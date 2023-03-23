package org.tms.server;

public class AdminPortal {
    private final IAdminService adminService;

    public AdminPortal(IAdminService adminService) {
        this.adminService = adminService;
    }

    public TruckDriver cancelTruck(int truckId) {
        return adminService.cancelTruck(truckId);
    }

    public boolean changeQueuePosition(int truckId, int newPosition) {
        return adminService.changeQueuePosition(truckId, newPosition);
    }

    public WarehouseState viewEntireWarehouseState() {
        return adminService.viewEntireWarehouseState();
    }
}
