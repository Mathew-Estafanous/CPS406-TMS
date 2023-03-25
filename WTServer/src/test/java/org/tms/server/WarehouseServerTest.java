package org.tms.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.tms.server.TruckState.LocationState.*;

@ExtendWith(MockitoExtension.class)
class WarehouseServerTest {

    private WarehouseServer warehouseServer;
    @Mock
    private TruckWaitingQueue truckWaitingQueue;
    @Mock
    private DockingAreaManager dockingAreaManager;
    @Mock
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        warehouseServer = new WarehouseServer(truckWaitingQueue, dockingAreaManager, notificationService);
    }

    @Test
    void givenValidCheckIn_availableDA_driverStartsUnload() {
        final TruckDriver driver = new TruckDriver(1, "John", Duration.ofHours(1));
        when(dockingAreaManager.isDockingAreaAvailable()).thenReturn(true);
        when(dockingAreaManager.startUnload(driver)).thenReturn(2);

        final TruckState truckState = warehouseServer.checkIn(driver);
        verify(dockingAreaManager).startUnload(driver);
        final TruckState state = new TruckState(driver, DOCKING_AREA, 2);
        assertEquals(state, truckState);
    }

    @Test
    void givenValidCheckIn_fullDA_driverAddedToWA() {
        final TruckDriver driver = new TruckDriver(1, "John", Duration.ofHours(1));
        when(dockingAreaManager.isDockingAreaAvailable()).thenReturn(false);
        when(truckWaitingQueue.queueTruck(driver)).thenReturn(2);
        when(truckWaitingQueue.getWaitTime(1)).thenReturn(Duration.ofMinutes(25));

        final TruckState truckState = warehouseServer.checkIn(driver);
        verify(truckWaitingQueue).queueTruck(driver);
        final TruckState state = new TruckState(driver, WAITING_AREA, 2, Duration.ofMinutes(25));
        assertEquals(state, truckState);
    }

    @Test
    void givenValidCheckOut_truckInDA_truckStopsUnloading() {
        final TruckDriver driver = new TruckDriver(1, "John", Duration.ofHours(1));
        when(dockingAreaManager.isTruckUnloading(1)).thenReturn(true);
        when(dockingAreaManager.stopUnload(1)).thenReturn(driver);

        final TruckState truckState = warehouseServer.checkOut(1);
        verify(dockingAreaManager).stopUnload(1);
        final TruckState state = new TruckState(driver, LEAVING, 0);
        assertEquals(state, truckState);
    }

    @Test
    void givenValidCheckOut_truckInQueue_removeFromQueueAndReturn() {
        final TruckDriver driver = new TruckDriver(1, "John", Duration.ofHours(1));
        when(dockingAreaManager.isTruckUnloading(1)).thenReturn(false);
        when(truckWaitingQueue.queuePosition(1)).thenReturn(1);
        when(truckWaitingQueue.cancelTruck(1)).thenReturn(driver);

        final TruckState truckState = warehouseServer.checkOut(1);
        verify(truckWaitingQueue).cancelTruck(1);
        final TruckState state = new TruckState(driver, LEAVING, 1);
        assertEquals(state, truckState);
    }

    @Test
    void givenCheckForState_truckInDA_returnDockingAreaState() {
        final TruckDriver driver = new TruckDriver(1, "John", Duration.ofHours(1));
        when(dockingAreaManager.retrieveUnloadingTruck(1))
                .thenReturn(Optional.of(Map.entry(2, driver)));

        final TruckState truckState = warehouseServer.getCurrentTruckState(1);
        final TruckState state = new TruckState(driver, DOCKING_AREA, 2);
        assertEquals(state, truckState);
    }

    @Test
    void givenCheckForState_truckInQueue_returnWaitingAreaState() {
        final TruckDriver driver = new TruckDriver(1, "John", Duration.ofHours(1));
        when(dockingAreaManager.retrieveUnloadingTruck(1))
                .thenReturn(Optional.empty());
        when(truckWaitingQueue.queuePosition(1)).thenReturn(2);
        when(truckWaitingQueue.getWaitTime(1)).thenReturn(Duration.ofHours(2));
        when(truckWaitingQueue.findTruckDriver(1)).thenReturn(driver);

        final TruckState truckState = warehouseServer.getCurrentTruckState(1);
        final TruckState state = new TruckState(driver, WAITING_AREA, 2, Duration.ofHours(2));
        assertEquals(state, truckState);
    }

    @Test
    void givenValidCancel_truckInDA_removeTruckFromDA() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        when(dockingAreaManager.isTruckUnloading(1)).thenReturn(true);
        when(dockingAreaManager.stopUnload(1)).thenReturn(johnDriver);

        when(truckWaitingQueue.dequeueNextTruck()).thenReturn(Optional.of(janeDriver));
        when(dockingAreaManager.startUnload(janeDriver)).thenReturn(2);

        final TruckDriver resultDriver = warehouseServer.cancelTruck(1);
        verify(dockingAreaManager).stopUnload(1);
        verify(dockingAreaManager).startUnload(janeDriver);
        verify(notificationService).notifyTruckStartedUnloading(janeDriver, 2);
        verify(notificationService).notifyTruckCancelled(1);
        assertEquals(johnDriver, resultDriver);
    }

    @Test
    void givenValidCancel_truckInQueue_removeTruckFromQueue() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        when(dockingAreaManager.isTruckUnloading(1)).thenReturn(false);
        when(truckWaitingQueue.queuePosition(1)).thenReturn(1);
        when(truckWaitingQueue.cancelTruck(1)).thenReturn(johnDriver);
        when(truckWaitingQueue.getQueueCurrentState()).thenReturn(List.of(janeDriver));
        when(truckWaitingQueue.getWaitTime(2)).thenReturn(Duration.ZERO);

        final TruckDriver resultDriver = warehouseServer.cancelTruck(1);
        verify(notificationService).notifyTruckCancelled(1);
        verify(notificationService).notifyTruckUpdatedState(2, 1, Duration.ZERO);
        assertEquals(johnDriver, resultDriver);
    }

    @Test
    void givenValidChangeQueue_correctlyChangeQueuePos() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        when(truckWaitingQueue.repositionTruck(1, 1)).thenReturn(1);
        when(truckWaitingQueue.getQueueCurrentState()).thenReturn(List.of(janeDriver, johnDriver));
        when(truckWaitingQueue.getWaitTime(2)).thenReturn(Duration.ZERO);
        when(truckWaitingQueue.getWaitTime(1)).thenReturn(Duration.ofHours(2));

        final boolean success = warehouseServer.changeQueuePosition(1, 1);
        assertTrue(success);
        verify(notificationService).notifyTruckUpdatedState(janeDriver.getTruckID(), 1, Duration.ZERO);
        verify(notificationService).notifyTruckUpdatedState(johnDriver.getTruckID(), 2, Duration.ofHours(2));
    }

    @Test
    void givenInvalidChangeQueue_returnFalse() {
        when(truckWaitingQueue.repositionTruck(1, 67)).thenReturn(-1);

        final boolean success = warehouseServer.changeQueuePosition(1, 67);
        assertFalse(success);
    }

    @Test
    void givenValidWarehouse_returnsCorrectWarehouseState() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        final TruckDriver bobDriver = new TruckDriver(3, "Jane", Duration.ofHours(2));
        when(truckWaitingQueue.getQueueCurrentState()).thenReturn(List.of(johnDriver, janeDriver));
        when(truckWaitingQueue.queuePosition(1)).thenReturn(1);
        when(truckWaitingQueue.getWaitTime(1)).thenReturn(Duration.ZERO);
        when(truckWaitingQueue.getWaitTime(2)).thenReturn(Duration.ofHours(1));
        when(truckWaitingQueue.queuePosition(2)).thenReturn(2);
        when(dockingAreaManager.getCurrentState()).thenReturn(Map.of(3, bobDriver));

        final List<TruckState> expectedWaitingState = List.of(
                new TruckState(johnDriver, WAITING_AREA, 1, Duration.ZERO),
                new TruckState(janeDriver, WAITING_AREA, 2, Duration.ofHours(1))
        );
        final List<TruckState> expectedDockingState = List.of(new TruckState(bobDriver, DOCKING_AREA, 3, Duration.ofHours(2)));
        final WarehouseState warehouseState = warehouseServer.viewEntireWarehouseState();
        assertEquals(expectedDockingState, warehouseState.dockingTruckDrivers());
        assertTrue(expectedWaitingState.containsAll(warehouseState.waitingTruckDrivers()));
        assertTrue(warehouseState.waitingTruckDrivers().containsAll(expectedWaitingState));
    }
}