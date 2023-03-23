package org.tms.server;

public interface ITruckService {

    /**
     * Handles a truck driver checking in to the warehouse.
     * @param driver The driver checking in.
     * @return The state of the truck after the check in.
     */
    TruckState checkIn(TruckDriver driver);

    /**
     * Handles a truck driver checking out of the warehouse.
     * @param truckId The id of the truck to check out.
     * @return The state of the truck after the checkout.
     */
    TruckState checkOut(int truckId);

    /**
     * Gets the current state of the truck.
     * @param truckId The id of the truck to get the state of.
     * @return The current state of the truck.
     */
    TruckState getCurrentTruckState(int truckId);
}