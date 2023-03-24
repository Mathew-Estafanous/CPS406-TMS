package org.tms.server;

    import java.util.List;

public record WarehouseState(List<TruckDriver> waitingTruckDrivers, List<TruckDriver> dockingTruckDrivers) {
}
