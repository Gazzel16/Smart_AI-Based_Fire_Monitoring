package com.example.smartai_basedfiremonitoring.Model.Sensors;

public class Flame {

    public Double flameOutput;
    public Boolean flameDetector;

    public Flame(){

    }

    public Flame(Double flameOutput, Boolean flameDetector){
        this.flameOutput = flameOutput;
        this.flameDetector = flameDetector;
    }

    public Double getFlameOutput() {
        return flameOutput;
    }
    public void setFlameOutput(Double flameOutput) {
        this.flameOutput = flameOutput;
    }

    public Boolean getFlameDetector() {
        return flameDetector;
    }
    public void setFlameDetector(Boolean flameDetector) {
        this.flameDetector = flameDetector;
    }

}
