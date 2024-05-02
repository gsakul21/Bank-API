package dev.codescreen;

public abstract class Response {

    private String userId;
    private String messageId;
    private Amount balance;

    public Response(String userId, String messageId, Amount balance)
    {
        this.userId = userId;
        this.messageId = messageId;
        this.balance = balance;
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

    public Amount getBalance() {
        return balance;
    }

    public void setBalance(Amount balance) {
        this.balance = balance;
    }

    
}
