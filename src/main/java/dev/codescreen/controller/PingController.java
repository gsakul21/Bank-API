package dev.codescreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.codescreen.schemas.Ping;
import dev.codescreen.service.PingService;

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
    public ResponseEntity<?> ping()
    {
        // Call to PingService.
        Object pingResp = pingService.ping();

        /*
         * Checking if it is either a Ping response which indicates a successful
         * call, or a ServerError which means something went wrong. Formats
         * ResponseEntity as either 200 (Ok) or 400 (Bad Request) accordingly.
         */

        if (pingResp.getClass() == Ping.class)
        {
            Ping response = (Ping) pingResp;

            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(pingResp);
    }

}
