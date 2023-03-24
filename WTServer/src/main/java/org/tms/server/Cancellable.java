package org.tms.server;

public interface Cancellable {

    /**
     * Cancels a truck that is either in the waiting area or docking area.
     * @param truckId the id of the truck to cancel
     * @return the driver of the truck that was cancelled
     * @throws IllegalArgumentException if the truck is not in the waiting area or docking area
     */
    TruckDriver cancelTruck(int truckId) throws IllegalArgumentException;
}
