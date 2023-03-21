package org.tms.server;

import java.util.Map;
import java.util.Optional;

public class DockingAreaManager {

    /**
     * Check if there is a DA available to dock a truck.
     * @return true if there is a DA available, false otherwise.
     */
    public boolean isDockingAreaAvailable() {
        return false;
    }

    /**
     * Checks if a truck with the given id is currently unloading.
     * @param truckId the truck id
     * @return true if the truck is unloading, false otherwise.
     */
    public boolean isTruckUnloading(int truckId) {
        return false;
    }

    /**
     * Assigns a DA to the truck and starts the unloading process.
     * @param driver the truck driver
     * @return the DA # assigned to the truck.
     */
    public int startUnload(TruckDriver driver) {
        return 0;
    }

    /**
     * Stops the unloading process and returns the truck driver.
     * @param truckId the truck id
     * @return the truck driver that has stopped unloading.
     */
    public TruckDriver stopUnload(int truckId) {
        return null;
    }

    /**
     * Retrieves the truck driver that is currently unloading. If no
     * truck with the given id is unloading, then an empty optional is returned.
     * @param truckId the truck id
     * @return The docking area number and the truck driver that is unloading.
     */
    public Optional<Map.Entry<Integer, TruckDriver>> retrieveUnloadingTruck(int truckId) {
        return Optional.empty();
    }

    public Map<Integer, TruckDriver> getCurrentState() {
        return null;
    }
}
