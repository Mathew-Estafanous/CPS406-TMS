package org.tms.server;

    import java.util.List;

public record WarehouseState(List<TruckState> waitingTruckDrivers, List<TruckState> dockingTruckDrivers) {
}
