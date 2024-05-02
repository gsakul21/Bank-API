package dev.codescreen;

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
