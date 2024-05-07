package dev.codescreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.codescreen.schemas.AuthorizationRequest;
import dev.codescreen.schemas.AuthorizationResponse;
import dev.codescreen.schemas.LoadRequest;
import dev.codescreen.schemas.LoadResponse;
import dev.codescreen.service.BankLedgerService;

/*
 * This is the a controller responsible for handling the /authorization
 * and /load endpoints of our service. It correctly maps the endpoints to their 
 * services responsible for dealing with the expected functionality of the endpoint.
 * 
 * For more detail as to what occurs for each endpoint, scroll further down to
 * learn more.
 */

@RestController
@RequestMapping("/")
public class BankLedgerController {

    @Autowired
    private BankLedgerService bankLedgerService;

    /**
     * @param authorizationRequest
     * @return ResponseEntity<AuthorizationResponse> or ResponseEntity<ServerError>
     * 
     * This is the function that maps the REST endpoint corresponding to the
     * authorization of funds functionality of the service. It calls the controller 
     * responsible for those features and returns either a AuthorizationResponse as specified or 
     * a ServerError object detailing what went wrong.
     */
    @PutMapping("/authorization")
    public ResponseEntity<?> authorization(@RequestBody AuthorizationRequest authorizationRequest)
    {

        // Calls the service responsible for handling fund authorization
        Object authResp = bankLedgerService.authorize(authorizationRequest);

        /*
         * Checking to see if service returned an error or a valid response
         * object, indicating success. Sending result as 201 (Created) or
         * 400 (Bad Request) accordingly.
         */
        if (authResp.getClass() == AuthorizationResponse.class)
        {
            /* 
             * Written under assumption that 201 (Created) is indicating creation
             * of event tracking this request, so it doesn't matter if the
             * authorization request is declined or approved, an event is made.
             */

            AuthorizationResponse response = (AuthorizationResponse) authResp;
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.badRequest().body(authResp);
    }

    
    /**
     * @param loadRequest
     * @return ResponseEntity<LoadResponse> or ResponseEntity<ServerError>
     * 
     * This is the function that maps the REST endpoint corresponding to the
     * load funds functionality of the service. It calls the controller responsible
     * for those features and returns either a LoadResponse as specified or 
     * a ServerError object detailing what went wrong.
     * 
     */
    @PutMapping("/load")
    public ResponseEntity<?> load(@RequestBody LoadRequest loadRequest)
    {
        // Calls the service responsible for loading funds to a user.  
        Object loadResp = bankLedgerService.load(loadRequest);

        /*
         * Checking to see if service returned an error or a valid response
         * object, indicating success. Sending result as 201 (Created) or
         * 400 (Bad Request) accordingly.
         */
        if (loadResp.getClass() == LoadResponse.class)
        {
            LoadResponse response = (LoadResponse) loadResp;
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.badRequest().body(loadResp);
    }

}
