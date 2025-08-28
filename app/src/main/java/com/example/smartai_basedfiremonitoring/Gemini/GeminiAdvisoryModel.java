package com.example.smartai_basedfiremonitoring.Gemini;

public class GeminiAdvisoryModel {
    private String title;
    private String description;

    public GeminiAdvisoryModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
