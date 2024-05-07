package dev.codescreen.schemas;

/*
 * Base form of a request in this service. Contains required details of a
 * request to the endpoints that consume them (/load and /authorization). 
 * 
 * See {@link AuthorizationRequest} and {@link LoadRequest} for related 
 * subclasses.
 */
public abstract class Request {

    /*
     * Basic fields of all requests. User id for the request, message id corresponding
     * to the request, and transaction amount with details on the asset.
     */
    private String userId;
    private String messageId;

    // To learn more about this field, refer to {@link Amount}
    private Amount transactionAmount;

    public Request(String userId, String messageId, Amount transactionAmount)
    {
        this.userId = userId;
        this.messageId = messageId;
        this.transactionAmount = transactionAmount;
    }

    // Getters and Setters for all attributes of the object.

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
