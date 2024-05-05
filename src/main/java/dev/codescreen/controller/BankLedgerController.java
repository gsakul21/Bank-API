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
import dev.codescreen.schemas.ResponseCode;
import dev.codescreen.service.BankLedgerService;

@RestController
@RequestMapping("/")
public class BankLedgerController {

    @Autowired
    private BankLedgerService bankLedgerService;

    @PutMapping("/authorization")
    public ResponseEntity<?> authorization(@RequestBody AuthorizationRequest authorizationRequest)
    {
        Object authResp = bankLedgerService.authorize(authorizationRequest);

        if (authResp.getClass() == AuthorizationResponse.class)
        {
            AuthorizationResponse response = (AuthorizationResponse) authResp;
            if (response.getResponse() == ResponseCode.DECLINED)
            {
                return ResponseEntity.badRequest().body(response);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.badRequest().body(authResp);
    }

    @PutMapping("/load")
    public ResponseEntity<?> load(@RequestBody LoadRequest loadRequest)
    {
        Object loadResp = bankLedgerService.load(loadRequest);

        if (loadResp.getClass() == LoadResponse.class)
        {
            LoadResponse response = (LoadResponse) loadResp;
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.badRequest().body(loadResp);
    }

}
