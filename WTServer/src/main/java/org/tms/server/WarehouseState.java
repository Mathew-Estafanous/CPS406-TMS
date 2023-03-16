package org.tms.server;

    import java.util.List;

public class WarehouseState {
    private final List<TruckDriver> waitingTruckDrivers;

    private final List<TruckDriver> dockingTruckDrivers;

    public WarehouseState(List<TruckDriver> waitingTruckDrivers, List<TruckDriver> dockingTruckDrivers) {
        this.waitingTruckDrivers = waitingTruckDrivers;
        this.dockingTruckDrivers = dockingTruckDrivers;
    }

    public List<TruckDriver> getWaitingTruckDrivers() {
        return waitingTruckDrivers;
    }

    public List<TruckDriver> getDockingTruckDrivers() {
        return dockingTruckDrivers;
    }
}
