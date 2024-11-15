package sop.models;

import java.time.LocalDateTime;

public class ContracItems {
    private int contractItemID;
    private int contractID;
    private int quoteItemID;
    private String itemDescription;
    private String status;
    private LocalDateTime createdAt;

    // Getters và Setters
    public int getContractItemID() {
        return contractItemID;
    }

    public void setContractItemID(int contractItemID) {
        this.contractItemID = contractItemID;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getQuoteItemID() {
        return quoteItemID;
    }

    public void setQuoteItemID(int quoteItemID) {
        this.quoteItemID = quoteItemID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
