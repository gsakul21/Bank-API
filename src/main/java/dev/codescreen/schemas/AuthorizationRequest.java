package dev.codescreen.schemas;

/* 
 * Implementation of AuthorizationRequest schema in service specification. Contains
 * all required details of an authorization request for a specified User. Builds
 * off the base class, {@link Request}.
 */

public class AuthorizationRequest extends Request {

    public AuthorizationRequest(String userId, String messageId, Amount transactionAmount)
    {
        super(userId, messageId, transactionAmount);
    }
}
