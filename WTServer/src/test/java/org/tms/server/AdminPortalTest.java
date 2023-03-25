package org.tms.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminPortalTest {
    private AdminPortal adminPortal;
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
        adminPortal = new AdminPortal(warehouseServer, warehouseServer);
    }

    @Test
    void givenValidCancel_truckInDA_removeTruckFromDA() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        when(dockingAreaManager.isTruckUnloading(1)).thenReturn(true);
        when(dockingAreaManager.stopUnload(1)).thenReturn(johnDriver);

        when(truckWaitingQueue.dequeueNextTruck()).thenReturn(Optional.of(janeDriver));
        when(dockingAreaManager.startUnload(janeDriver)).thenReturn(2);

        final TruckDriver resultDriver = adminPortal.cancelTruck(1);
        verify(dockingAreaManager).stopUnload(1);
        verify(dockingAreaManager).startUnload(janeDriver);
        verify(notificationService).notifyTruckStartedUnloading(resultDriver, 2);
        verify(notificationService).notifyTruckCancelled(1);
        assertEquals(johnDriver, resultDriver);
    }

    @Test
    void givenValidCancel_truckInQueue_removeTruckFromQueue() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        when(dockingAreaManager.isTruckUnloading(1)).thenReturn(false);
        when(truckWaitingQueue.queuePosition(1)).thenReturn(0);
        when(truckWaitingQueue.cancelTruck(1)).thenReturn(johnDriver);
        when(truckWaitingQueue.getQueueCurrentState()).thenReturn(List.of(janeDriver));
        when(truckWaitingQueue.getWaitTime(2)).thenReturn(Duration.ZERO);

        final TruckDriver resultDriver = adminPortal.cancelTruck(1);
        verify(notificationService).notifyTruckCancelled(1);
        verify(notificationService).notifyTruckUpdatedState(2, 0, Duration.ZERO);
        assertEquals(johnDriver, resultDriver);
    }

    @Test
    void givenValidChangeQueue_correctlyChangeQueuePos() {
        final TruckDriver johnDriver = new TruckDriver(1, "John", Duration.ofHours(1));
        final TruckDriver janeDriver = new TruckDriver(2, "Jane", Duration.ofHours(2));
        when(truckWaitingQueue.repositionTruck(1, 0)).thenReturn(0);
        when(truckWaitingQueue.getQueueCurrentState()).thenReturn(List.of(janeDriver, johnDriver));
        when(truckWaitingQueue.getWaitTime(2)).thenReturn(Duration.ZERO);
        when(truckWaitingQueue.getWaitTime(1)).thenReturn(Duration.ofHours(2));

        final boolean success = adminPortal.changeQueuePosition(1, 0);
        assertTrue(success);
        verify(notificationService).notifyTruckUpdatedState(janeDriver.getTruckID(), 0, Duration.ZERO);
        verify(notificationService).notifyTruckUpdatedState(johnDriver.getTruckID(), 1, Duration.ofHours(2));
    }

    @Test
    void givenInvalidChangeQueue_returnFalse() {
        when(truckWaitingQueue.repositionTruck(1, 67)).thenReturn(-1);

        final boolean success = adminPortal.changeQueuePosition(1, 67);
        assertFalse(success);
    }

    @Test
    void viewEntireWarehouseState() {
    }
}
