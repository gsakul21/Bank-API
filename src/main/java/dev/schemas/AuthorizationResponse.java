package dev.schemas;

import jakarta.validation.constraints.NotNull;
/*
 * Implementation of the AuthorizationResponse schema utilized by the service
 * in the "/authorization" endpoint, as specified in the service outline. 
 * 
 * Holds all the required components of a response from the Service, as well as if
 * the authorization was successful or not.
 */

public class AuthorizationResponse extends Response {

    /*
     * Indication of how the request went. See {@link ResponseCode} for more
     * details.
     */
    @NotNull
    private ResponseCode response;

    public AuthorizationResponse(String userId, String messageId, ResponseCode response, Amount balance)
    {
        super(userId, messageId, balance);

        this.response = response;
    }

    // Getter and setter for object specific attribute.

    public ResponseCode getResponse() {
        return response;
    }

    public void setResponse(ResponseCode response) {
        this.response = response;
    }
    
}
