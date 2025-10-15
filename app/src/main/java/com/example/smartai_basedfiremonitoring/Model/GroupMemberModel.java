package com.example.smartai_basedfiremonitoring.Model;

public class GroupMemberModel {

    private int imageResId;
    private String courseSection;
    private String name;
    private String description;

    public GroupMemberModel(int imageResId, String courseSection, String name, String description) {
        this.imageResId = imageResId;
        this.courseSection = courseSection;
        this.name = name;
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(String courseSection) {
        this.courseSection = courseSection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
