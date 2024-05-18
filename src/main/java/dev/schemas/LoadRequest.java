package dev.schemas;

/* 
 * Implementation of LoadRequest schema in service specification. Contains
 * all required details of a load request for a specified User. Builds
 * off the base class, {@link Request}.
 */
public class LoadRequest extends Request {

    public LoadRequest(String userId, String messageId, Amount transactionAmount)
    {
        super(userId, messageId, transactionAmount);
    }
   
}
