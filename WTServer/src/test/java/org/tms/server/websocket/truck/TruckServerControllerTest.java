package org.tms.server.websocket.truck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.server.ITruckService;
import org.tms.server.TruckDriver;
import org.tms.server.TruckState;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.tms.server.TruckState.LocationState.*;
import static org.tms.server.websocket.truck.TruckMessage.TruckMessageType.*;

@ExtendWith(MockitoExtension.class)
class TruckServerControllerTest {

    @Mock
    private ITruckService truckService;

    private TruckServerController controller;
    private Map<Integer, Session> sessionMap;

    @BeforeEach
    void setUp() {
        sessionMap = new HashMap<>();
        controller = new TruckServerController(truckService, sessionMap);
    }

    @Test
    void givenOpenSession_addedToSessionMap(@Mock Session session) {
        controller.onOpen(session, 1);
        assertEquals(session, sessionMap.get(1));
    }

    @Test
    void givenCheckInMessage_TruckServiceCalled_thenReturnResult(@Mock Session session) throws EncodeException, IOException {
        final TruckMessage message = new TruckMessage(1, CHECK_IN, "John Doe", "PT1H");
        final TruckDriver expectedDriver = new TruckDriver(1, "John Doe", Duration.ofHours(1));
        final TruckState truckStateResult = new TruckState(expectedDriver, DOCKING_AREA, 1);

        when(truckService.checkIn(expectedDriver)).thenReturn(truckStateResult);
        RemoteEndpoint.Basic basicRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(basicRemote);
        assertDoesNotThrow(() -> controller.onMessage(session, message));

        final TruckMessage resultMessage = new TruckMessage(truckStateResult, CHECK_IN);
        verify(truckService).checkIn(expectedDriver);
        verify(basicRemote).sendObject(resultMessage);
    }

    @Test
    void givenCheckOutMessage_TruckServiceCalled_thenReturnResult(@Mock Session session) throws EncodeException, IOException {
        final TruckMessage message = new TruckMessage(1, CHECK_OUT, "John Doe", "PT1H");
        final TruckDriver expectedDriver = new TruckDriver(1, "John Doe", Duration.ofHours(1));
        final TruckState truckStateResult = new TruckState(expectedDriver, DOCKING_AREA, 1);

        when(truckService.checkOut(1)).thenReturn(truckStateResult);
        RemoteEndpoint.Basic basicRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(basicRemote);
        assertDoesNotThrow(() -> controller.onMessage(session, message));

        final TruckMessage resultMessage = new TruckMessage(truckStateResult, CHECK_OUT);
        verify(truckService).checkOut(1);
        verify(basicRemote).sendObject(resultMessage);
    }

    @Test
    void givenStateUpdateMessage_TruckServiceCalled_thenReturnResult(@Mock Session session) throws EncodeException, IOException {
        final TruckMessage message = new TruckMessage(1, STATE_UPDATE, UNKNOWN, 1);
        final TruckState truckStateResult = new TruckState(new TruckDriver(1, "", Duration.ZERO), WAITING_AREA, 6);

        when(truckService.getCurrentTruckState(1)).thenReturn(truckStateResult);
        RemoteEndpoint.Basic basicRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(basicRemote);
        assertDoesNotThrow(() -> controller.onMessage(session, message));

        final TruckMessage resultMessage = new TruckMessage(truckStateResult, STATE_UPDATE);
        verify(truckService).getCurrentTruckState(1);
        verify(basicRemote).sendObject(resultMessage);
    }

    @Test
    void givenSessionToClose_removedFromSessionMap(@Mock Session session) {
        sessionMap.put(1, session);
        controller.onClose(session);
        assertFalse(sessionMap.containsKey(1));
    }
}