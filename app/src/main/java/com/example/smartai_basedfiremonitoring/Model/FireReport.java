package com.example.smartai_basedfiremonitoring.Model;

public class FireReport {
    private String reportId;
    private String userId;
    private String reporterName;
    private boolean confirmation;

    private boolean falseReport;
    private String description;
    private String timeReported;

    public FireReport() {
    }

    public FireReport(String reportId, String userId, String reporterName, boolean confirmation,
                      boolean falseReport, String description, String timeReported) {
        this.reportId = reportId;
        this.userId = userId;
        this.reporterName = reporterName;
        this.confirmation = confirmation;
        this.falseReport = falseReport;
        this.description = description;
        this.timeReported = timeReported;
    }

    public String getReportId(){
        return reportId;
    }
public void setReportId(String reportId){
        this.reportId =  reportId;
}
    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId =  userId;
    }
    public String getReporterName() {
        return reporterName;
    }

    public boolean isConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Boolean confirmation){
        this.confirmation = confirmation;
    }

    public boolean isFalseReport(){
        return falseReport;
    }

    public void setFalseReport(Boolean falseReport){
        this.falseReport = falseReport;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeReported() {
        return timeReported;
    }
}
