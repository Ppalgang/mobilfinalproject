package com.example.mobilfinalproject;

import java.util.List;
import android.text.TextUtils;

public class Photo {
    private String photoId; // Add this field for photoId
    private String imageUrl; // Resmin Base64 formatında String'i
    private String userEmail; // Kullanıcının e-posta adresi
    private List<String> labels;
    private int likeCount;
    private int dislikeCount;

    public Photo(String photoId, String imageUrl, String userEmail, List<String> labels, int likeCount, int dislikeCount) {
        this.photoId = photoId; // Initialize photoId
        this.imageUrl = imageUrl;
        this.userEmail = userEmail;
        this.labels = labels;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public Photo() {
        // Boş kurucu metodu
    }

    public String getPhotoId() {
        return photoId; // Getter method for photoId
    }

    public String getUsername() {
        return userEmail;
    }

    public String getLabelsAsString() {
        return TextUtils.join(", ", labels);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
