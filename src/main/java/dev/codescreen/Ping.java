package dev.codescreen;

import java.time.Instant;

public class Ping {

    public String serverTime;

    public Ping(Instant serverTime)
    {
        this.serverTime = serverTime.toString();
    }
}
