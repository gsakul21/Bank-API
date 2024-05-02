package dev.codescreen.schemas;

public class AuthorizationRequest extends Request {

    public AuthorizationRequest(String userId, String messageId, Amount transactionAmount)
    {
        super(userId, messageId, transactionAmount);
    }
}
