package sop.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QuoteItems {
    // Fields
    private int quoteItemId;
    private int quoteId;         // Foreign key from tbl_quotes
    private int serviceId;       // Foreign key from tbl_services
    private String itemDescription;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private String notes;

    public boolean selected;

    // Constructors
    public QuoteItems() {
        // Default constructor
    }

    public QuoteItems(int quoteItemId, int quoteId, int serviceId, String itemDescription, int quantity, 
                      BigDecimal unitPrice, LocalDateTime createdAt, String notes) {
        this.quoteItemId = quoteItemId;
        this.quoteId = quoteId;
        this.serviceId = serviceId;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice.multiply(new BigDecimal(quantity)); // Calculate total price
        this.createdAt = createdAt;
        this.notes = notes;
    }

    // Getters and Setters
    public int getQuoteItemId() {
        return quoteItemId;
    }

    public void setQuoteItemId(int quoteItemId) {
        this.quoteItemId = quoteItemId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
    	this.totalPrice = totalPrice;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "QuoteItems{" +
                "quoteItemId=" + quoteItemId +
                ", quoteId=" + quoteId +
                ", serviceId=" + serviceId +
                ", itemDescription='" + itemDescription + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                ", notes='" + notes + '\'' +
                '}';
    }
}

