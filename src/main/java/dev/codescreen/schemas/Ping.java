package dev.codescreen.schemas;

import java.time.Instant;

/*
 * Ping schema, as detailed in service specification file. Contains one attribute,
 * server time.
 */
public class Ping {

    public String serverTime;

    public Ping(Instant serverTime)
    {
        this.serverTime = serverTime.toString();
    }
}
