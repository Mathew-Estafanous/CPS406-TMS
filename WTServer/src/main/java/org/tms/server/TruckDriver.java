package org.tms.server;

import java.time.Duration;
import java.util.Objects;

public class TruckDriver {
    private final int truckID;
    private final String driverName;
    private final Duration estimatedDockingTime;

    public TruckDriver(int truckID, String driverName, Duration estimatedDockingTime) {
        this.truckID = truckID;
        this.driverName = driverName;
        this.estimatedDockingTime = estimatedDockingTime;
    }

    public Duration getEstimatedDockingTime() {
        return estimatedDockingTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getTruckID() {
        return truckID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckDriver that = (TruckDriver) o;
        return truckID == that.truckID && driverName.equals(that.driverName) && Objects.equals(estimatedDockingTime, that.estimatedDockingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truckID, driverName, estimatedDockingTime);
    }
}
