package org.tms.server;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * TruckWaitingQueue represents a queue of trucks waiting to dock.
 */
public class TruckWaitingQueue {
    private final List<TruckDriver> queue;

    public TruckWaitingQueue() {
        this(new ArrayList<>());
    }

    public TruckWaitingQueue(List<TruckDriver> queue) {
        this.queue = queue;
    }

    /**
     * Get entire state of the queue.
     * @return a list of {@link TruckDriver}s
     */
    public List<TruckDriver> getQueueCurrentState() {
        return new ArrayList<>(queue);
    }

    /**
     * Adds a truck to the queue and returns the position of the truck in the queue.
     * @param driver The truck to add to the queue.
     * @return The position of the truck in the queue.
     */
    public int queueTruck(TruckDriver driver) {
        queue.add(driver);
        return queue.size();
    }

    /**
     * Removes the truck the front of the queue and returns the truck.
     * @return The truck at the front of the queue.
     */
    public TruckDriver dequeueNextTruck() {
        return queue.remove(0);
    }

    /**
     * Returns the position of the truck in the queue.
     * @param truckId The id of the truck to find.
     * @return The position of the tuck in the queue or -1 if the truck is not found.
     */
    public int queuePosition(int truckId) {
        for (int i = 0; i < queue.size(); i++) {
            if (queue.get(i).getTruckID() == truckId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Repositions the truck in the queue and returns the new position of the truck.
     * @param truckID The id of the truck to reposition.
     *                If the truckID is not found, the queue is not modified and -1 is returned.
     * @param newPosition The new position of the truck.
     *                    If the new position is greater than the size of the queue, the truck is moved to the end of the queue.
     *                    If the new position is less than 0, the truck is moved to the front of the queue.
     * @return The new position of the truck.
     */
    public int repositionTruck(int truckID, int newPosition) {
        TruckDriver driver = findTruckDriver(truckID);
        if (driver == null) {
            return -1;
        }
        if (newPosition >= queue.size()) {
            newPosition = queue.size() - 1;
        }
        if (newPosition < 0) {
            newPosition = 0;
        }
        queue.add(newPosition, driver);
        return newPosition;
    }

    /**
     * Get the wait time for specific truck in the queue.
     * @param truckID The id of the truck to get the wait time for.
     *                If the truckID is not found, null is returned.
     * @return The wait time for the truck.
     */
    public Duration getWaitTime(int truckID) {
        TruckDriver driver = findTruckDriver(truckID);
        if (driver == null) {
            return null;
        }
        Duration waitTime = Duration.ZERO;
        for (TruckDriver truck : queue) {
            if (truck.getTruckID() == truckID) {
                break;
            }
            waitTime = waitTime.plus(truck.getEstimatedDockingTime());
        }
        return waitTime;
    }

    /**
     * Cancel a truck in the queue and return that truck which was cancelled.
     * @param truckID The id of the truck to cancel.
     *                If the truckID is not found, null is returned.
     * @return The truck which was cancelled.
     */
    public TruckDriver cancelTruck(int truckID) {
        TruckDriver driver = findTruckDriver(truckID);
        if (driver == null) {
            return null;
        }
        queue.remove(driver);
        return driver;
    }

    /**
     * Finds a truck with the given truckID in the queue.
     * @param truckID The id of the truck to find.
     * @return The truck with the given truckID or null if the truck is not found.
     */
    public TruckDriver findTruckDriver(int truckID) {
        return queue.stream().filter(truck -> truck.getTruckID() == truckID).findFirst().orElse(null);
    }
}
