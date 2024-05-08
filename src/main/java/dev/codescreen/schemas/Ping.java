package dev.codescreen.schemas;

import java.time.Instant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/*
 * Ping schema, as detailed in service specification file. Contains one attribute,
 * server time.
 */
public class Ping {

    @NotEmpty
    @NotNull
    public String serverTime;

    public Ping(Instant serverTime)
    {
        this.serverTime = serverTime.toString();
    }
}
