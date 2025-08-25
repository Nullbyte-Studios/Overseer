package com.nullbyte.overseer.util.database.dao;

import java.time.Instant;

public class Note {
    private int id;
    private String playerUuid;
    private String moderatorUuid;
    private String message;
    private Instant createdAt;
}
