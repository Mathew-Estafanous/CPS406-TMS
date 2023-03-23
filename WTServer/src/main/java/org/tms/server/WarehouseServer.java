package org.tms.server;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.tms.server.TruckState.LocationState.*;

public class WarehouseServer implements ITruckService, IAdminService {

    private final TruckWaitingQueue waitingQueue;
    private final DockingAreaManager dockingArea;
    private final NotificationService notificationService;

    public WarehouseServer(TruckWaitingQueue waitingQueue, DockingAreaManager dockingArea, NotificationService notificationService) {
        this.waitingQueue = waitingQueue;
        this.dockingArea = dockingArea;
        this.notificationService = notificationService;
    }

    @Override
    public TruckState checkIn(TruckDriver driver) {
        if (dockingArea.isDockingAreaAvailable()) {
            final int dockingNumber = dockingArea.startUnload(driver);
            return new TruckState(driver, DOCKING_AREA, dockingNumber);
        } else {
            final int queuePosition = waitingQueue.queueTruck(driver);
            return new TruckState(driver, WAITING_AREA, queuePosition);
        }
    }

    @Override
    public TruckState checkOut(int truckId) {
        final TruckDriver driver = dockingArea.stopUnload(truckId);
        // get the next truck in the queue and start unloading it.
        startUnloadingOfNextInQueue();
        return new TruckState(driver, LEAVING, 0);
    }

    private void startUnloadingOfNextInQueue() {
        final Optional<TruckDriver> nextTruckOptional = waitingQueue.dequeueNextTruck();
        if (nextTruckOptional.isEmpty()) return;
        final TruckDriver nextTruck = nextTruckOptional.get();
        final int dockingNumber = dockingArea.startUnload(nextTruck);
        notificationService.notifyTruckStartedUnloading(nextTruck.getTruckID(), dockingNumber);

        waitingQueue.getQueueCurrentState().forEach(truck -> {
            final Duration waitTime = waitingQueue.getWaitTime(truck.getTruckID());
            final int queuePosition = waitingQueue.queuePosition(truck.getTruckID());
            notificationService.notifyTruckUpdatedState(truck.getTruckID(), queuePosition, waitTime);
        });
    }

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
            return new TruckState(null, UNKNOWN, -1);
        }

        final var waitTime = waitingQueue.getWaitTime(truckId);
        final int queuePosition = waitingQueue.queuePosition(truckId);
        return new TruckState(driver, WAITING_AREA, queuePosition, waitTime);
    }

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
            if (driver == null) throw new IllegalArgumentException("Truck not found");
            notifyAllAffectedTrucksInQueue(waitingQueue.queuePosition(position));
            notificationService.notifyTruckCancelled(truckId);
            return driver;
        }
    }

    @Override
    public boolean changeQueuePosition(int truckId, int newPosition) {
        final int pos = waitingQueue.repositionTruck(truckId, newPosition);
        if (pos == -1) return false;
        notifyAllAffectedTrucksInQueue(pos);
        return true;
    }

    private void notifyAllAffectedTrucksInQueue(int pos) {
        final List<TruckDriver> queue = waitingQueue.getQueueCurrentState();
        for (int queuePos = pos; queuePos < queue.size(); queuePos++) {
            final TruckDriver truck = queue.get(queuePos);
            final Duration waitTime = waitingQueue.getWaitTime(truck.getTruckID());
            notificationService.notifyTruckUpdatedState(truck.getTruckID(), queuePos, waitTime);
        }
    }

    @Override
    public WarehouseState viewEntireWarehouseState() {
        final Map<Integer, TruckDriver> currentState = dockingArea.getCurrentState();
        final List<TruckDriver> queue = waitingQueue.getQueueCurrentState();
        return new WarehouseState(currentState.values().stream().toList(), queue);
    }
}
