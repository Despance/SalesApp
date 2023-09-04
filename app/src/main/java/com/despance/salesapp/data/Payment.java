package com.despance.salesapp.data;

public class Payment {

    private String type;
    private String description;
    private float amount;
    private String timestamp;

    public Payment(String type, String description, float amount, String timestamp) {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Payment [type=" + type + ", description=" + description + ", amount=" + amount + ", timestamp="
                + timestamp + "]";
    }

}
