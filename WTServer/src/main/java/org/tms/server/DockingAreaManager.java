package org.tms.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DockingAreaManager {

    // Map of Docking Number to Truck Driver.
    private final Map<Integer, TruckDriver> dockingMap;
    private final int totalDockingAreas;

    public DockingAreaManager(int totalDockingAreas) {
        this(new HashMap<>(), totalDockingAreas);
    }

    public DockingAreaManager(Map<Integer, TruckDriver> dockingMap, int totalDockingAreas) {
        this.dockingMap = dockingMap;
        this.totalDockingAreas = totalDockingAreas;
    }

    /**
     * Check if there is a DA available to dock a truck.
     * @return true if there is a DA available, false otherwise.
     */
    public boolean isDockingAreaAvailable() {
        return dockingMap.size() < totalDockingAreas;
    }

    /**
     * Checks if a truck with the given id is currently unloading.
     * @param truckId the truck id
     * @return true if the truck is unloading, false otherwise.
     */
    public boolean isTruckUnloading(int truckId) {
        return dockingMap.values().stream()
                .anyMatch(truckDriver -> truckDriver.getTruckID() == truckId);
    }

    /**
     * Assigns a DA to the truck and starts the unloading process.
     * @param driver the truck driver
     * @return the DA # assigned to the truck.
     */
    public int startUnload(TruckDriver driver) throws IllegalArgumentException {
        for (int i = 1; i <= totalDockingAreas; i++) {
            if (!dockingMap.containsKey(i)) {
                dockingMap.put(i, driver);
                return i;
            }
        }
        throw new IllegalArgumentException("No docking area available.");
    }

    /**
     * Stops the unloading process and returns the truck driver.
     * @param truckId the truck id
     * @return the truck driver that has stopped unloading.
     */
    public TruckDriver stopUnload(int truckId) {
        final var driverOptional = dockingMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getTruckID() == truckId)
                .findFirst();
        final Map.Entry<Integer, TruckDriver> driver = driverOptional.orElseThrow(() -> new IllegalArgumentException("Truck not found."));
        return dockingMap.remove(driver.getKey());
    }

    /**
     * Retrieves the truck driver that is currently unloading. If no
     * truck with the given id is unloading, then an empty optional is returned.
     * @param truckId the truck id
     * @return The docking area number and the truck driver that is unloading.
     */
    public Optional<Map.Entry<Integer, TruckDriver>> retrieveUnloadingTruck(int truckId) {
        return dockingMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().getTruckID() == truckId)
                .findFirst();
    }

    /**
     * Returns the current state of the docking area.
     * @return A map of the DA # to the truck driver that is unloading.
     */
    public Map<Integer, TruckDriver> getCurrentState() {
        return Map.copyOf(dockingMap);
    }
}
