package org.tms.server;

public class TruckState {
    private final TruckDriver truckDriver;
    private final Boolean isInWaitingArea;
    // position represents either the position in the waiting area or the position in the docking area.
    private final int position;

    public TruckState(TruckDriver truckDriver, Boolean isInWaitingArea, int position) {
        this.truckDriver = truckDriver;
        this.isInWaitingArea = isInWaitingArea;
        this.position = position;
    }

    public TruckDriver getTruckDriver() {
        return truckDriver;
    }

    public Boolean getInWaitingArea() {
        return isInWaitingArea;
    }

    public int getPosition() {
        return position;
    }
}
