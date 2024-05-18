package dev.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.schemas.Ping;
import dev.service.PingService;

/*
 * 
 * This is the a controller responsible for handling the /ping endpoint. It correctly 
 * maps the endpoint to the service responsible for dealing with the expected 
 * functionality of the endpoint.
 * 
 */

@RestController
@RequestMapping("/")
public class PingController {

    @Autowired
    private PingService pingService;

    /**
     * @return ResponseEntity<Ping> or ResponseEntity<ServerError>
     * 
     * This function sets up the endpoint "/ping" such that it will accept
     * GET requests and goes ahead to call the service in charge of 
     * handling pinging the server.
     * 
     */
    @GetMapping("/ping")
    public ResponseEntity<Ping> ping()
    {
        return ResponseEntity.ok().body(pingService.ping());
    }

}
