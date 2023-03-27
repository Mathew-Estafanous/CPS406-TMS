package org.tms.server;

/**
 * The basic functionality that can be performed by Admins.
 */
public interface IAdminService extends Cancellable {

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
