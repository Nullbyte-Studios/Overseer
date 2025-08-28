package com.nullbyte.overseer.util.structures;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private int id;
    private String playerUuid;
    private String moderatorUuid;
    private String message;
    private Instant createdAt;
}
