package dev.codescreen.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import dev.codescreen.schemas.Ping;

/*
 * This service has one primary responsibility, providing the functionality 
 * for the "/ping" endpoint of the API service. 
 */
@Service
public class PingService {

    
    /**
     * @return Ping 
     * 
     * The function trys to get the server time at the moment, in the same format
     * as the example providied in service.yml. It uses the java Instant time
     * object.
     */
    public Ping ping()
    {
        return new Ping(Instant.now());
    }

}
