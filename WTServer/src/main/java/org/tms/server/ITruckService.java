package org.tms.server;

/**
 * Core Functionality reuired by the TruckServerController to fulfill requests made by the truck driver on the app.
 */
public interface ITruckService extends Cancellable {

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

    /**
     * Checks if the truck id is already taken. If so, then the truck driver cannot check in.
     * @param truckID The id of the truck to check.
     * @return True if the truck id is already taken, false otherwise.
     */
    boolean isTruckIDTaken(int truckID);
}
