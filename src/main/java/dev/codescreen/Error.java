package dev.codescreen;

public abstract class Error {

    public String message;
    public String code;

    public Error(String message)
    {
        this.message = message;
    }

    public Error(String message, String code)
    {
        this.code = code;
    }
}
