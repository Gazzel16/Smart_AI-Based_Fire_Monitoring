package com.example.smartai_basedfiremonitoring.Model;

public class AdminDashBoardModel {
    private int imageResId; // Drawable resource
    private String title;
    private int progress;    // 0-100
    private String count;

    public AdminDashBoardModel(int imageResId, String title, int progress, String count) {
        this.imageResId = imageResId;
        this.title = title;
        this.progress = progress;
        this.count = count;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public String getCount() {
        return count;
    }
}
