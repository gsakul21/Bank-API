package dev.codescreen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.codescreen.schemas.Ping;
import dev.codescreen.service.PingService;

@RestController
@RequestMapping("/")
public class PingController {

    @Autowired
    private PingService pingService;

    @GetMapping("/ping")
    public ResponseEntity<?> ping()
    {
        Object pingResp = pingService.ping();

        if (pingResp.getClass() == Ping.class)
        {
            Ping response = (Ping) pingResp;

            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.badRequest().body(pingResp);
    }

}
