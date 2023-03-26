package org.tms.server;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.util.Objects;

/**
 * TruckState repersents what the current state of the truckDriver
 */
public class TruckState {
    private final TruckDriver truckDriver;
    private final LocationState locationState;
    // position represents either the position in the waiting area or the position in the docking area.
    private final int position;
    private final Duration estimatedTime;

    public TruckState(int truckID) {
        this(new TruckDriver(truckID), LocationState.UNKNOWN, -1, Duration.ZERO);
    }

    public TruckState(TruckDriver truckDriver, LocationState locationState, int position) {
        this(truckDriver, locationState, position, truckDriver.getEstimatedDockingTime());
    }

    public TruckState(TruckDriver truckDriver, LocationState locationState, int position, Duration estimatedTime) {
        this.truckDriver = truckDriver;
        this.locationState = locationState;
        this.position = position;
        this.estimatedTime = estimatedTime;
    }

    /**
     * Obtains the ID of the truck.
     * @return The truckID.
     */
    public int getTruckID() {
        return truckDriver.getTruckID();
    }

    /**
     * Obtains the TruckDriver.
     * @return the truckDriver.
     */
    public TruckDriver getTruckDriver() {
        return truckDriver;
    }

    /**
     * Obtains the Location of the truck.
     * @return The Location of the truck.
     */
    public LocationState getLocationState() {
        return locationState;
    }

    /**
     * Obtains the position of the truck.
     * @return The truck's position.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Obtains the Estimated time it takes driver to unload.
     * @return The estimated time.
     */
    public Duration getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Determines if two truck states are equal.
     * @param o repersents the other truck state
     * @return whether the two states are equivalent .
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckState that = (TruckState) o;
        return position == that.position && truckDriver.equals(that.truckDriver) && locationState == that.locationState && Objects.equals(estimatedTime, that.estimatedTime);
    }

    /**
     * Obtains hashcode of the truck state.
     * @return The hashcode of a single truck state.
     */
    @Override
    public int hashCode() {
        return Objects.hash(truckDriver, locationState, position, estimatedTime);
    }

    /**
     * Holds the name of a certain location state.
     * @return The name of the location.
     */
    public enum LocationState {
        @SerializedName("waiting_area")
        WAITING_AREA,
        @SerializedName("docking_area")
        DOCKING_AREA,
        @SerializedName("leaving")
        LEAVING,
        @SerializedName("unknown")
        UNKNOWN
    }
}
