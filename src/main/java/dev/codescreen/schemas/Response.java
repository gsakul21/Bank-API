package dev.codescreen.schemas;

/*
 * Base form of a response object in the API. Utilized to return information
 * from a related service after it is done processing the input (a request). 
 * Holds the common fields of a Response for the API in general.
 * 
 * See {@link LoadResponse} and {@link AuthorizationResponse} for related
 * subclasses.
 */
public abstract class Response {

    /*
     * The user id related to the request (i.e. the user the request is for)
     * and the message id of the request, as well as the balance of the user
     * after processing the request.
     */
    private String userId;
    private String messageId;

    // To learn more about this attribute, please refer to {@link Amount}
    private Amount balance;

    public Response(String userId, String messageId, Amount balance)
    {
        this.userId = userId;
        this.messageId = messageId;
        this.balance = balance;
    }

    // Getters and Setters of the object's attributes.

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
