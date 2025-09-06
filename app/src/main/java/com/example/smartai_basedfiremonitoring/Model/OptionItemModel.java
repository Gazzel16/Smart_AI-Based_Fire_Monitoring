package com.example.smartai_basedfiremonitoring.Model;

public class OptionItemModel {
    private String title;
    private int iconResId; // drawable resource ID

    public OptionItemModel(String title, int iconResId) {
        this.title = title;
        this.iconResId = iconResId;
    }

    // Getter and Setter
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
