package org.tms.server;

public interface IAdminService {

    /**
     * Cancels a truck that is either in the waiting area or docking area.
     * @param truckId the id of the truck to cancel
     * @return the driver of the truck that was cancelled
     * @throws IllegalArgumentException if the truck is not in the waiting area or docking area
     */
    TruckDriver cancelTruck(int truckId) throws IllegalArgumentException;



    /**
     * Changes the position of a truck in the waiting area.
     * @param truckId the id of the truck to change the position of
     * @param newPosition the new position of the truck (0 is the first position)
     * @return true if the truck was moved, false if the truck was not in the waiting area
     */
    boolean changeQueuePosition(int truckId, int newPosition);

    /**
     * Returns the entire state of the warehouse including both the state of the
     * waiting area and the docking areas
     * @return the entire state of the warehouse
     */
    WarehouseState viewEntireWarehouseState();
}
