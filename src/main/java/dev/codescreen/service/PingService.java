package dev.codescreen.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import dev.codescreen.schemas.Ping;
import dev.codescreen.schemas.ServerError;

/*
 * This service has one primary responsibility, providing the functionality 
 * for the "/ping" endpoint of the API service. 
 */
@Service
public class PingService {

    
    /**
     * @return Ping or ServerError
     * 
     * The function trys to get the server time at the moment, in the same format
     * as the example providied in service.yml. It uses the java Instant time
     * object. If there is any issue catches and returns it as a ServerError.
     */
    public Object ping()
    {
        try
        {
            return new Ping(Instant.now());
        }
        catch (Exception e)
        {
            return new ServerError(e.getMessage());
        }    
    }

}
