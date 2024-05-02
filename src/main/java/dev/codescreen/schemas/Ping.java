package dev.codescreen.schemas;

import java.time.Instant;

public class Ping {

    public String serverTime;

    public Ping(Instant serverTime)
    {
        this.serverTime = serverTime.toString();
    }
}
