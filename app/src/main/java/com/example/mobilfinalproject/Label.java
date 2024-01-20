package com.example.mobilfinalproject;

public class Label {
    private String labelName;
    private String description;

    public Label() {
        // Boş kurucu metot gerekli
    }

    public Label(String labelName, String description) {
        this.labelName = labelName;
        this.description = description;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getDescription() {
        return description;
    }
}
