package sop.models;

import java.time.LocalDateTime;

public class Banner {
    private int bannerId;
    private String description;
    private LocalDateTime createdAt;
    private int status;
    public Banner() {
    }

    // Parameterized constructor
    public Banner(int bannerId, String description, LocalDateTime createdAt, int status) {
        this.bannerId = bannerId;
        this.description = description;
        this.createdAt = createdAt;
        this.status = status;
    }
    // Getters and Setters
    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
