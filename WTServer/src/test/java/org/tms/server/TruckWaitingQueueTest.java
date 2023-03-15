package org.tms.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckWaitingQueueTest {

    private TruckWaitingQueue truckWaitingQueue;

    @BeforeEach
    void setUp() {
        final List<TruckDriver> queue = new ArrayList<>();
        queue.add(new TruckDriver(1, "John", Duration.ofMinutes(10)));
        queue.add(new TruckDriver(2, "Jane", Duration.ofMinutes(20)));
        queue.add(new TruckDriver(3, "Adam", Duration.ofMinutes(13)));
        truckWaitingQueue = new TruckWaitingQueue(queue);

    }

    @Test
    void testQueueingTrucksWorks() {
        final TruckDriver driver = new TruckDriver(4, "Bob", Duration.ofMinutes(15));
        final int position = truckWaitingQueue.queueTruck(driver);
        assertEquals(4, position);
    }

    @Test
    void testAllCasesOfRepositioningTrucks() {
        // move truck 2 to position 1
        final int position = truckWaitingQueue.repositionTruck(2, 1);
        assertEquals(1, position);

        // move truck 2 to position 10 which is greater than the size of the queue,
        // so it should be moved to the end of the queue
        final int position2 = truckWaitingQueue.repositionTruck(2, 10);
        assertEquals(3, position2);

        // move truck 2 to position -1 which is less than 0 so it should be moved to the front of the queue
        final int position3 = truckWaitingQueue.repositionTruck(2, -1);
        assertEquals(0, position3);

        // move truck 5 to position 1 which is not in the queue, so it should return -1
        final int position4 = truckWaitingQueue.repositionTruck(5, 1);
        assertEquals(-1, position4);
    }

    @Test
    void testAllCasesForWaitTime() {
        // the wait time for truck 1 should be 0
        final Duration waitTime = truckWaitingQueue.getWaitTime(1);
        assertEquals(Duration.ZERO, waitTime);

        // the wait time for truck 2 should be 10 minutes
        final Duration waitTime2 = truckWaitingQueue.getWaitTime(2);
        assertEquals(Duration.ofMinutes(10), waitTime2);

        // the wait time for truck 3 should be 30 minutes
        final Duration waitTime3 = truckWaitingQueue.getWaitTime(3);
        assertEquals(Duration.ofMinutes(30), waitTime3);

        // the wait time for truck 4 should be -1 because it is not in the queue
        final Duration waitTime4 = truckWaitingQueue.getWaitTime(4);
        assertNull(waitTime4);
    }

    @Test
    void testCancellingTruck() {
        final TruckDriver cancelTruck = truckWaitingQueue.cancelTruck(1);
        assertEquals(cancelTruck, new TruckDriver(1, "John", Duration.ofMinutes(10)));
    }
}