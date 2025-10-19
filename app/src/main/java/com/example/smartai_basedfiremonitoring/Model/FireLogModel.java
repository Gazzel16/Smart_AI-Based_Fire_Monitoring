package com.example.smartai_basedfiremonitoring.Model;

public class FireLogModel {
    private String dateTime;

    public FireLogModel(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

}
