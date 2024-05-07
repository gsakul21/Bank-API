package dev.codescreen.schemas;

/*
 * The general form of an error response from the API, as specified in the 
 * service specification. Builds off the base class {@link Error}.
 */
public class ServerError extends Error {

    public ServerError(String message)
    {
        super(message);
    }

    public ServerError(String message, String code)
    {
        super(message, code);
    }
}
