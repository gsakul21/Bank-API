package dev.codescreen.schemas;

public abstract class Request {
    private String userId;
    private String messageId;
    private Amount transactionAmount;

    public Request(String userId, String messageId, Amount transactionAmount)
    {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionAmount = transactionAmount;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getMessageId() {
        return messageId;
    }
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    public Amount getTransactionAmount() {
        return transactionAmount;
    }
    public void setTransactionAmount(Amount transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    
}
