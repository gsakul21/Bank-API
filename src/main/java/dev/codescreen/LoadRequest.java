package dev.codescreen;

public class LoadRequest extends Request{

    public LoadRequest(String userId, String messageId, Amount transactionAmount)
    {
        super(userId, messageId, transactionAmount);
    }
   
}
