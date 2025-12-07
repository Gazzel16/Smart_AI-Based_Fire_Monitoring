package com.example.smartai_basedfiremonitoring.Model;

import java.util.List;

public class SensorMaintenanceInstructionModel {

    private String title;                  // Single title
    private List<String> instructionsEN;   // English instructions
    private List<String> instructionsTAG;  // Tagalog instructions
    private int imageResId;

    public SensorMaintenanceInstructionModel(String title,
                                             List<String> instructionsEN,
                                             List<String> instructionsTAG,
                                             int imageResId) {
        this.title = title;
        this.instructionsEN = instructionsEN;
        this.instructionsTAG = instructionsTAG;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }
    public List<String> getInstructionsEN() { return instructionsEN; }
    public List<String> getInstructionsTAG() { return instructionsTAG; }
    public int getImageResId() { return imageResId; }
}
