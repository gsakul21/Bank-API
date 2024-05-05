package dev.codescreen.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import dev.codescreen.schemas.ServerError;

@Service
public class PingService {

    public Object ping()
    {
        try
        {
            return Instant.now().toString();
        }
        catch (Exception e)
        {
            return new ServerError(e.getMessage());
        }    
    }

}
