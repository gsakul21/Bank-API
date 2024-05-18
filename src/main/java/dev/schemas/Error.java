package dev.schemas;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

/*
 * Base class for errors in the Service. Contains common required information of
 * an error.
 * 
 * See {@link ServerError} for related subclass.
 */
public abstract class Error {

    // Error message and code (if applicable)
    @Size(min = 1)
    public String message;

    @Nullable
    @Size(min = 1)
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
