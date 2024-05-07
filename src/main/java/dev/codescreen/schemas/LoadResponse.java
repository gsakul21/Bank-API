package dev.codescreen.schemas;

/*
 * Implementation of the LoadResponse schema utilized by the service
 * in the "/load" endpoint, as specified in the service specification. 
 * 
 * Holds all the required components of a response from the Service, 
 * see {@link Response} to learn more.
 */
public class LoadResponse extends Response {

    public LoadResponse(String userId, String messageId, Amount balance){
        super(userId, messageId, balance);
    }

}
