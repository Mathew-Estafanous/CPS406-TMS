package org.tms.server;

public class AdminPortal {
    private final IAdminService adminService;
    private final Cancellable can;

    public AdminPortal(IAdminService adminService, Cancellable can) {
        this.adminService = adminService;
        this.can = can;
    }

    public TruckDriver cancelTruck(int truckId) {
        try {
            return can.cancelTruck(truckId);
        } catch (Exception e) {
            System.out.println("Unable to find truckID: " + e.getMessage());
            return null;
        }
    }

    public boolean changeQueuePosition(int truckId, int newPosition) {
        try {
            return adminService.changeQueuePosition(truckId, newPosition);
        } catch (Exception e) {
            System.out.println("Unable to change truck position: " + e.getMessage());
            return false;
        }

    }

    public WarehouseState viewEntireWarehouseState() {
        try {
            return adminService.viewEntireWarehouseState();
        } catch (Exception e) {
            System.out.println("Unable to view Warehouse state: " + e.getMessage());
            return null;
        }
    }
}
