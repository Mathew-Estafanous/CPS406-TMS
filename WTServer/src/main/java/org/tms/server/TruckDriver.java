package org.tms.server;

import java.time.Duration;
import java.util.Objects;

/**
 * TruckDriver hold basic information about the TruckDriver.
 */
public class TruckDriver {
    private final int truckID;
    private final String driverName;
    private final Duration estimatedDockingTime;

    public TruckDriver(int truckID) {
        this(truckID, "", Duration.ZERO);
    }

    public TruckDriver(int truckID, String driverName, Duration estimatedDockingTime) {
        this.truckID = truckID;
        this.driverName = driverName;
        this.estimatedDockingTime = estimatedDockingTime;
    }

    /**
     * Gets the time of the EstimatedDockingTime.
     * @return the estimated docking time.
     */
    public Duration getEstimatedDockingTime() {
        return estimatedDockingTime;
    }

    /**
     * Gets the name of the Truck Driver
     * @return driverName.
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * Gets the TruckID
     * @return TruckID.
     */
    public int getTruckID() {
        return truckID;
    }

    /**
     * Checks if two TruckDrivers are the same
     * @param o repersents the other truck
     * @return whether both TruckDrivers are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckDriver that = (TruckDriver) o;
        return truckID == that.truckID && driverName.equals(that.driverName) && Objects.equals(estimatedDockingTime, that.estimatedDockingTime);
    }

    /**
     * Gets tje hashcode of the TD object
     * @return the HashCode of a specific ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(truckID, driverName, estimatedDockingTime);
    }
}
