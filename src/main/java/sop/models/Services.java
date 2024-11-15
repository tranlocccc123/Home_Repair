package sop.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Services {
    // Fields
    private int serviceId;
    private String serviceName;
    private String description;
    private LocalDateTime createdAt;
    private BigDecimal price;
    private int status;

    // Constructors
    public Services() {
       
    }

    public Services(int serviceId, String serviceName, String description, LocalDateTime createdAt, BigDecimal price, int status) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.createdAt = createdAt;
        this.price = price;
        this.status = status;
    }

    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Services{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", status=" + status +
                '}';
    }
}
