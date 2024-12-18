package sop.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class Payment {

    private String namePayment;
    private int contractId;
    private Timestamp createdAt;
    private BigDecimal amountMoney;
    private String stageDescription;
    private String paymentStage;
    private String status;  // Possible values: 'Pending', 'Confirmed', 'Contracted'

    // Constructors
    public Payment() {
        // Default constructor
    }

    public Payment(int contractId, String namePayment, String status, String paymentStage) {
        this.contractId = contractId;
        this.namePayment = namePayment;
        this.status = status;
        this.setPaymentStage(paymentStage);
        this.setCreatedAt(Timestamp.from(Instant.now()));
    }

    // Getters and Setters
    public String getNamePayment() {
        return namePayment;
    }

    public void setNamePayment(String name) {
        this.namePayment = name;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractid) {
        this.contractId = contractid;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaymentStage() {
        return paymentStage;
    }

    public void setPaymentStage(String stage) {
        this.paymentStage = stage;
    }    

    public String getStageDescription() {
        return stageDescription;
    }

    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }  

    public BigDecimal getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(BigDecimal amount) {
        this.amountMoney = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment copy()
    {
        Payment clone = new Payment();
        clone.setNamePayment(namePayment);
        clone.setContractId(contractId);
        clone.setAmountMoney(amountMoney);
        clone.setCreatedAt(createdAt);
        clone.setStatus(status);
        clone.setPaymentStage(paymentStage);
        clone.setStageDescription(stageDescription);
        return clone;
    }

}
