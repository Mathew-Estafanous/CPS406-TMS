package org.tms.server;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.util.Objects;

public class TruckState {
    private final TruckDriver truckDriver;
    private final LocationState locationState;
    // position represents either the position in the waiting area or the position in the docking area.
    private final int position;
    private final Duration estimatedTime;

    public TruckState(TruckDriver truckDriver, LocationState locationState, int position) {
        this(truckDriver, locationState, position, Duration.ZERO);
    }

    public TruckState(TruckDriver truckDriver, LocationState locationState, int position, Duration estimatedTime) {
        this.truckDriver = truckDriver;
        this.locationState = locationState;
        this.position = position;
        this.estimatedTime = estimatedTime;
    }

    public int getTruckID() {
        return truckDriver.getTruckID();
    }

    public TruckDriver getTruckDriver() {
        return truckDriver;
    }

    public LocationState getLocationState() {
        return locationState;
    }

    public int getPosition() {
        return position;
    }

    public Duration getEstimatedTime() {
        return estimatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckState that = (TruckState) o;
        return position == that.position && truckDriver.equals(that.truckDriver) && locationState == that.locationState && Objects.equals(estimatedTime, that.estimatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(truckDriver, locationState, position, estimatedTime);
    }

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
