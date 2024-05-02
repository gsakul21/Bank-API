package dev.codescreen;

public class AuthorizationResponse extends Response {

    private ResponseCode response;

    public AuthorizationResponse(String userId, String messageId, ResponseCode response, Amount balance)
    {
        super(userId, messageId, balance);

        this.response = response;
    }

    public ResponseCode getResponse() {
        return response;
    }

    public void setResponse(ResponseCode response) {
        this.response = response;
    }
    
}
