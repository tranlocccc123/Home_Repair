package sop.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Quotes {
    // Fields
    private int quoteId;
    private int employeeId;
    private int customerId;
    private LocalDateTime quoteDate;
    private BigDecimal totalAmount;
    private String status;  // Possible values: 'Pending', 'Confirmed', 'Contracted'

    // Constructors
    public Quotes() {
        // Default constructor
    }

    public Quotes(int quoteId, int employeeId, int customerId, LocalDateTime quoteDate, BigDecimal totalAmount, String status) {
        this.quoteId = quoteId;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.quoteDate = quoteDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getQuoteDate() {
        return quoteDate;
    }

    public void setQuoteDate(LocalDateTime quoteDate) {
        this.quoteDate = quoteDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method for easy printing
    @Override
    public String toString() {
        return "Quotes{" +
                "quoteId=" + quoteId +
                ", employeeId=" + employeeId +
                ", customerId=" + customerId +
                ", quoteDate=" + quoteDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
