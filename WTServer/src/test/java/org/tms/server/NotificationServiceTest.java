package org.tms.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.server.websocket.TruckMessage;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Test
    void givenNotifyStartUnload_receiveInDockingAreaMessage(@Mock Session session) throws EncodeException, IOException {
        final NotificationService notificationService = new NotificationService(Map.of(1, session));
        final RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);
        notificationService.notifyTruckStartedUnloading(1, 3);
        final TruckMessage expectedMessage = new TruckMessage(1, TruckMessage.MessageType.STATE_UPDATE, TruckState.LocationState.DOCKING_AREA, 3);
        verify(mockRemote).sendObject(expectedMessage);
    }

    @Test
    void givenNotifyStatusUpdate_receiveUpdatedTimeAndPosition(@Mock Session session) throws EncodeException, IOException {
        final NotificationService notificationService = new NotificationService(Map.of(1, session));
        final RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);
        notificationService.notifyTruckUpdatedState(1, 3, Duration.ofMinutes(15));
        final TruckMessage expectedMessage = new TruckMessage(1, TruckMessage.MessageType.STATE_UPDATE, TruckState.LocationState.WAITING_AREA, 3, Duration.ofMinutes(15));
        verify(mockRemote).sendObject(expectedMessage);
    }
}