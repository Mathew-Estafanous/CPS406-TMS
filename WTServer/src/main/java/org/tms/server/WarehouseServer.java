package org.tms.server;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.tms.server.TruckState.LocationState.*;

/**
 * WarehouseServer contains the vital components linking the waitingQueue and
 * dockingArea and NotificationServes while getting obtaining the correct
 * information for the ITruckService and IAdminService.
 */
public class WarehouseServer implements ITruckService, IAdminService {

    private final TruckWaitingQueue waitingQueue;
    private final DockingAreaManager dockingArea;
    private final NotificationService notificationService;

    public WarehouseServer(TruckWaitingQueue waitingQueue, DockingAreaManager dockingArea, NotificationService notificationService) {
        this.waitingQueue = waitingQueue;
        this.dockingArea = dockingArea;
        this.notificationService = notificationService;
    }

    /**
     * Checks into a TD into the Warehouse by ensuring the truckID has not been used
     * by another truck driver. If the customer is valid, it assigns them a spot in
     * either the docking area or the waiting queue.
     */
    @Override
    public TruckState checkIn(TruckDriver driver) {
        if(isTruckIDTaken(driver.getTruckID())) {
            return new TruckState(driver.getTruckID());
        }

        if (dockingArea.isDockingAreaAvailable()) {
            final int dockingNumber = dockingArea.startUnload(driver);
            return new TruckState(driver, DOCKING_AREA, dockingNumber);
        } else {
            final int queuePosition = waitingQueue.queueTruck(driver);
            final Duration waitTime = waitingQueue.getWaitTime(driver.getTruckID());
            return new TruckState(driver, WAITING_AREA, queuePosition, waitTime);
        }
    }

    /**
     * Checks out of a TD into the Warehouse when the truck driver is done unloading.
     * It gets the next truck to start unloading, and it notifies all other trucks that
     * their position in line has changed.
     */
    @Override
    public TruckState checkOut(int truckId) {
        if (dockingArea.isTruckUnloading(truckId)) {
            final TruckDriver driver = dockingArea.stopUnload(truckId);
            // get the next truck in the queue and start unloading it.
            startUnloadingOfNextInQueue();
            return new TruckState(driver, LEAVING, 0);
        } else {
            int position = waitingQueue.queuePosition(truckId);
            if (position == -1) {
                throw new IllegalArgumentException(String.format("No truck with ID %d is in the warehouse.", truckId));
            }
            TruckDriver truckDriver = waitingQueue.cancelTruck(truckId);
            notifyAllAffectedTrucksInQueue(position);
            return new TruckState(truckDriver, LEAVING, position);
        }
    }

    /**
     * Unloads the truck that is next in the waiting queue.
     */
    private void startUnloadingOfNextInQueue() {
        final Optional<TruckDriver> nextTruckOptional = waitingQueue.dequeueNextTruck();
        if (nextTruckOptional.isEmpty()) return;
        final TruckDriver nextTruck = nextTruckOptional.get();
        final int dockingNumber = dockingArea.startUnload(nextTruck);
        notificationService.notifyTruckStartedUnloading(nextTruck, dockingNumber);

        waitingQueue.getQueueCurrentState().forEach(truck -> {
            final Duration waitTime = waitingQueue.getWaitTime(truck.getTruckID());
            final int queuePosition = waitingQueue.queuePosition(truck.getTruckID());
            notificationService.notifyTruckUpdatedState(truck.getTruckID(), queuePosition, waitTime);
        });
    }

    /**
     * Gets the current state of the Truck. Possible TuckStates include "waiting_are",
     * "docking_area", "leaving", or "unknown".
     */
    @Override
    public TruckState getCurrentTruckState(int truckId) {
        final var driverEntry = dockingArea.retrieveUnloadingTruck(truckId);
        if (driverEntry.isPresent()) {
            final Integer dockingNumber = driverEntry.get().getKey();
            final TruckDriver driver = driverEntry.get().getValue();
            return new TruckState(driver, DOCKING_AREA, dockingNumber);
        }
        final TruckDriver driver = waitingQueue.findTruckDriver(truckId);
        if (driver == null) {
            return new TruckState(truckId);
        }

        final var waitTime = waitingQueue.getWaitTime(truckId);
        final int queuePosition = waitingQueue.queuePosition(truckId);
        return new TruckState(driver, WAITING_AREA, queuePosition, waitTime);
    }

    /**
     * Determines if the truckID is in use by another truck driver in the Warehouse System.
     */
    @Override
    public boolean isTruckIDTaken(int truckID) {
        return dockingArea.retrieveUnloadingTruck(truckID).isPresent() ||
                waitingQueue.findTruckDriver(truckID) != null;
    }

    /**
     * Cancels a truck in the warehouse regardless of their state. It notifies all other
     * trucks about their position.
     * @throws IllegalArgumentException if the truckID is not a valid ID.
     */
    @Override
    public TruckDriver cancelTruck(int truckId) throws IllegalArgumentException {
        if (dockingArea.isTruckUnloading(truckId)) {
            final TruckDriver driver = dockingArea.stopUnload(truckId);
            notificationService.notifyTruckCancelled(truckId);
            startUnloadingOfNextInQueue();
            return driver;
        } else {
            final int position = waitingQueue.queuePosition(truckId);
            final TruckDriver driver = waitingQueue.cancelTruck(truckId);
            if (driver == null) {
                throw new IllegalArgumentException("Truck not found");
            }
            notifyAllAffectedTrucksInQueue(position);
            notificationService.notifyTruckCancelled(truckId);
            return driver;
        }
    }

    /**
     * Changes the position of a truck in the waiting queue.
     */
    @Override
    public boolean changeQueuePosition(int truckId, int newPosition) {
        final int pos = waitingQueue.repositionTruck(truckId, newPosition);
        if (pos == -1) return false;
        notifyAllAffectedTrucksInQueue(pos);
        return true;
    }

    /**
     * Notifies all users that their position has changed.
     * @param pos The position of the truck that has changed.
     */
    private void notifyAllAffectedTrucksInQueue(int pos) {
        final List<TruckDriver> queue = waitingQueue.getQueueCurrentState();
        for (int queuePos = pos-1; queuePos < queue.size(); queuePos++) {
            final TruckDriver truck = queue.get(queuePos);
            final Duration waitTime = waitingQueue.getWaitTime(truck.getTruckID());
            notificationService.notifyTruckUpdatedState(truck.getTruckID(), queuePos+1, waitTime);
        }
    }

    /**
     * Provides a view of the docking area and the waiting queue.
     */
    @Override
    public WarehouseState viewEntireWarehouseState() {
        final Map<Integer, TruckDriver> currentState = dockingArea.getCurrentState();
        final List<TruckState> dockingAreaState = currentState.entrySet().stream()
                .map(state -> new TruckState(state.getValue(), DOCKING_AREA, state.getKey())).toList();
        final List<TruckDriver> queue = waitingQueue.getQueueCurrentState();
        final List<TruckState> waitingQueueState = queue.stream().map(truck -> new TruckState(truck,
                WAITING_AREA,
                waitingQueue.queuePosition(truck.getTruckID()),
                waitingQueue.getWaitTime(truck.getTruckID()))).toList();
        return new WarehouseState(waitingQueueState, dockingAreaState);
    }
}
