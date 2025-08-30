package com.example.smartai_basedfiremonitoring.Model;

public class FireReport {

    private String reporterName;
    private boolean confirmation;
    private String description;
    private String timeReported;

    public FireReport(String reporterName, boolean confirmation, String description, String timeReported) {
        this.reporterName = reporterName;
        this.confirmation = confirmation;
        this.description = description;
        this.timeReported = timeReported;
    }

    public String getReporterName() {
        return reporterName;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeReported() {
        return timeReported;
    }
}
