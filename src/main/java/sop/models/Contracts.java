package sop.models;


public class Contracts {
    private int contractId;
    private String contractCode;
    private int userId;
    private int quoteId;
    private java.sql.Timestamp contractDate;
    private java.sql.Timestamp signDate;
    private double deposit;
    private String paymentStages;
    private String status;

    // Getters and Setters
    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(int quoteId) {
        this.quoteId = quoteId;
    }

    public java.sql.Timestamp getContractDate() {
        return contractDate;
    }

    public void setContractDate(java.sql.Timestamp contractDate) {
        this.contractDate = contractDate;
    }

    public java.sql.Timestamp getSignDate() {
        return signDate;
    }

    public void setSignDate(java.sql.Timestamp signDate) {
        this.signDate = signDate;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public String getPaymentStages() {
        return paymentStages;
    }

    public void setPaymentStages(String paymentStages) {
        this.paymentStages = paymentStages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

