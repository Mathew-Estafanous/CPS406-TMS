package org.tms.server.websocket.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tms.server.IAdminService;
import org.tms.server.TruckDriver;
import org.tms.server.TruckState;
import org.tms.server.WarehouseState;
import org.tms.server.Authenticator;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminPortalTest {

    @Mock
    private IAdminService adminService;
    @Mock
    private Authenticator authenticator;

    private AdminPortal portal;

    @BeforeEach
    void setUp() {
        portal = new AdminPortal(adminService, authenticator);
    }

    @Test
    void givenValidLoginMessage_returnCookieAndSuccessMessage(@Mock Session session) throws IOException, EncodeException {
        final AdminMessage loginMessage = new AdminMessage(AdminMessage.AdminMessageType.LOGIN, "admin", "admin");
        when(authenticator.toCredentials(loginMessage.getUsername(), loginMessage.getPassword())).thenReturn(Optional.of("someToken"));
        final RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);
        portal.onMessage(session, loginMessage);

        verify(mockRemote).sendText(contains("Set-Cookie: token=someToken"));
        verify(mockRemote).sendObject(new AdminMessage(AdminMessage.AdminMessageType.LOGIN, 0, 0));
    }

    @Test
    void givenInvalidLogin_returnFailedMessage(@Mock Session session) throws EncodeException, IOException {
        final AdminMessage loginMessage = new AdminMessage(AdminMessage.AdminMessageType.LOGIN, "admin", "admin");
        when(authenticator.toCredentials(loginMessage.getUsername(), loginMessage.getPassword())).thenReturn(Optional.empty());
        final RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);
        portal.onMessage(session, loginMessage);

        verify(mockRemote).sendObject(new AdminMessage(AdminMessage.AdminMessageType.FAILED, 0, 0));
    }

    @Test
    void givenInvalidCredentials_allAdminMessagesFail(@Mock Session session) throws EncodeException, IOException {
        final AdminMessage adminMessage = new AdminMessage(AdminMessage.AdminMessageType.VIEW_STATE, 0, 0);
        when(session.getUserProperties()).thenReturn(Map.of("token", "invalidToken"));
        when(authenticator.authenticate("invalidToken")).thenReturn(false);

        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);

        portal.onMessage(session, adminMessage);
        verify(mockRemote).sendObject(new AdminMessage(AdminMessage.AdminMessageType.FAILED, 0, 0));
    }

    @Test
    void givenViewStateCommand_withValidCredentials_AdminServiceCalled(@Mock Session session) throws EncodeException, IOException {
        final AdminMessage viewStateMessage = new AdminMessage(AdminMessage.AdminMessageType.VIEW_STATE, 0, 0);
        when(session.getUserProperties()).thenReturn(Map.of("token", "validToken"));
        when(authenticator.authenticate("validToken")).thenReturn(true);

        final TruckState waitingState = new TruckState(new TruckDriver(1), TruckState.LocationState.WAITING_AREA, 1);
        final TruckState dockingState = new TruckState(new TruckDriver(2), TruckState.LocationState.DOCKING_AREA, 2);
        final WarehouseState resultWarehouseState = new WarehouseState(List.of(waitingState), List.of(dockingState));
        when(adminService.viewEntireWarehouseState()).thenReturn(resultWarehouseState);

        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);

        portal.onMessage(session, viewStateMessage);
        verify(adminService).viewEntireWarehouseState();
        verify(mockRemote).sendObject(new AdminMessage(waitingState, AdminMessage.AdminMessageType.VIEW_STATE));
        verify(mockRemote).sendObject(new AdminMessage(dockingState, AdminMessage.AdminMessageType.VIEW_STATE));
    }

    @Test
    void givenSuccessfulChangePosition_withValidCredentials_SuccessResponse(@Mock Session session) throws EncodeException, IOException {
        final AdminMessage changePositionMessage = new AdminMessage(AdminMessage.AdminMessageType.CHANGE_POSITION, 1, 2);
        when(session.getUserProperties()).thenReturn(Map.of("token", "validToken"));
        when(authenticator.authenticate("validToken")).thenReturn(true);

        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);

        when(adminService.changeQueuePosition(1, 2)).thenReturn(true);
        portal.onMessage(session, changePositionMessage);
        verify(mockRemote).sendObject(new AdminMessage(AdminMessage.AdminMessageType.CHANGE_POSITION, 1, 2));
    }

    @Test
    void givenFailedChangePosition_withValidCredentials_FailedResponse(@Mock Session session) throws EncodeException, IOException {
        final AdminMessage changePositionMessage = new AdminMessage(AdminMessage.AdminMessageType.CHANGE_POSITION, 1, 2);
        when(session.getUserProperties()).thenReturn(Map.of("token", "validToken"));
        when(authenticator.authenticate("validToken")).thenReturn(true);

        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);

        when(adminService.changeQueuePosition(1, 2)).thenReturn(false);
        portal.onMessage(session, changePositionMessage);
        verify(mockRemote).sendObject(new AdminMessage(AdminMessage.AdminMessageType.FAILED, 1, 2));
    }

    @Test
    void givenCancelTruck_withValidCredentials_returnCancelledTruck(@Mock Session session) throws EncodeException, IOException {
        final AdminMessage cancelMessage = new AdminMessage(AdminMessage.AdminMessageType.CANCEL, 1, 0);
        when(session.getUserProperties()).thenReturn(Map.of("token", "validToken"));
        when(authenticator.authenticate("validToken")).thenReturn(true);

        RemoteEndpoint.Basic mockRemote = mock(RemoteEndpoint.Basic.class);
        when(session.getBasicRemote()).thenReturn(mockRemote);

        final TruckDriver cancelledTruck = new TruckDriver(1);
        when(adminService.cancelTruck(1)).thenReturn(cancelledTruck);
        portal.onMessage(session, cancelMessage);
        verify(mockRemote).sendObject(new AdminMessage(cancelledTruck, AdminMessage.AdminMessageType.CANCEL));
    }
}