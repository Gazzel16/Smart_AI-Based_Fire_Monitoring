package com.example.smartai_basedfiremonitoring.Model;

public class MaintenanceLogsModel {
    private String lastMaintenance;
    private boolean isMaintenance;

    public MaintenanceLogsModel(String lastMaintenance, boolean isMaintenance) {
        this.lastMaintenance = lastMaintenance;

        this.isMaintenance = isMaintenance;
    }

    public String getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(String lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public boolean isMaintenance() {
        return isMaintenance;
    }

    public void setMaintenance(boolean maintenance) {
        isMaintenance = maintenance;
    }
}

