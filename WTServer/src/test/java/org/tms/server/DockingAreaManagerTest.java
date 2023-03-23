package org.tms.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DockingAreaManagerTest {

    private DockingAreaManager dockingAreaManager;

    private Map<Integer, TruckDriver> driverMap;

    @BeforeEach
    void setUp() {
        driverMap = new HashMap<>(Map.of(
                1, new TruckDriver(32, "John", Duration.ofMinutes(30)),
                2, new TruckDriver(76, "Bob", Duration.ofMinutes(30))
        ));
        dockingAreaManager = new DockingAreaManager(driverMap, 3);
    }

    @Test
    void whenThereIsSpace_isDockingAreaAvailable_returnsTrue() {
        assertTrue(dockingAreaManager.isDockingAreaAvailable());
    }

    @Test
    void whenThereIsNoSpace_isDockingAreaAvailable_returnsFalse() {
        driverMap.put(3, new TruckDriver(8, "Alice", Duration.ofMinutes(30)));
        assertFalse(dockingAreaManager.isDockingAreaAvailable());
    }

    @Test
    void whenTruckIsUnloading_returnTrue() {
        assertTrue(dockingAreaManager.isTruckUnloading(32));
    }

    @Test
    void whenTruckIsNotUnloading_returnFalse() {
        assertFalse(dockingAreaManager.isTruckUnloading(2));
    }

    @Test
    void givenValidStartUnload_addTruckToMap_returnDockingNumber() {
        final TruckDriver driver = new TruckDriver(1, "Alice", Duration.ofMinutes(30));
        assertEquals(3, dockingAreaManager.startUnload(driver));
        assertTrue(driverMap.containsKey(3));
    }

    @Test
    void givenValidStopUnload_removedTruckFromMap_returnTruckDriver() {
        final TruckDriver driver = dockingAreaManager.stopUnload(32);
        assertEquals(32, driver.getTruckID());
        assertFalse(driverMap.containsValue(driver));
    }

    @Test
    void givenRetrieveUnloadTruck_withCorrectID_returnDriverAndNumber() {
        final var driverOptional = dockingAreaManager.retrieveUnloadingTruck(76);
        assertTrue(driverOptional.isPresent());

        assertEquals(2, driverOptional.get().getKey());
        assertEquals(76, driverOptional.get().getValue().getTruckID());
    }

    @Test
    void givenRetrieveUnloadTruck_withIncorrectID_returnEmptyOptional() {
        final var driverOptional = dockingAreaManager.retrieveUnloadingTruck(1);
        assertTrue(driverOptional.isEmpty());
    }

    @Test
    void givenRequestForCurrentState_returnCopyOfEntireState() {
        final var currentState = dockingAreaManager.getCurrentState();
        assertEquals(2, currentState.size());
        final var expectedMap = new HashMap<>(Map.of(
                1, new TruckDriver(32, "John", Duration.ofMinutes(30)),
                2, new TruckDriver(76, "Bob", Duration.ofMinutes(30))
        ));
        assertEquals(expectedMap, currentState);
    }
}