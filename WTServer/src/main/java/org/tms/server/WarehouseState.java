package org.tms.server;

    import java.util.List;
/**
 * The values of the Warehouse Sate.
 */
    public record WarehouseState(List<TruckState> waitingTruckDrivers, List<TruckState> dockingTruckDrivers) {
}
