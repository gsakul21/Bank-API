package dev.codescreen.schemas;

/*
 * Base class for errors in the Service. Contains common required information of
 * an error.
 * 
 * See {@link ServerError} for related subclass.
 */
public abstract class Error {

    // Error message and code (if applicable)
    public String message;
    public String code;

    // Potential constructors, designed such that code is optional.
    public Error(String message)
    {
        this.message = message;
    }

    public Error(String message, String code)
    {
        this.code = code;
    }

    // Getters and Setters for object attributes. 

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    
}
