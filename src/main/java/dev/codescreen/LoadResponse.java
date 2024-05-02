package dev.codescreen;

public class LoadResponse extends Response {

    public LoadResponse(String userId, String messageId, Amount balance){
        super(userId, messageId, balance);
    }

}
