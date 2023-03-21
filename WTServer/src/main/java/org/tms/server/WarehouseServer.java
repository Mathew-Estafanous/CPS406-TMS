package org.tms.server;

import static org.tms.server.TruckState.LocationState.*;

public class WarehouseServer implements ITruckService {

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
        if (dockingArea.isAreaAvailable()) {
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
        return new TruckState(driver, LEAVING, 0);
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
}
