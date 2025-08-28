package com.nullbyte.overseer.util.database;

import com.nullbyte.overseer.util.structures.Note;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseUtils {
    private DatabaseUtils() {
        // Utility class
    }
    
    // Player methods
    public static void insertPlayer(String uuid) throws SQLException {
        String sql = "INSERT IGNORE INTO players (uuid) VALUES (?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        }
    }
    
    public static boolean playerExists(String uuid) throws SQLException {
        String sql = "SELECT 1 FROM players WHERE uuid = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    // Note methods
    public static int addNote(String playerUuid, String moderatorUuid, String message) throws SQLException {
        // Ensure player exists first
        insertPlayer(playerUuid);
        
        String sql = "INSERT INTO notes (player_uuid, moderator_uuid, message) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, playerUuid);
            stmt.setString(2, moderatorUuid);
            stmt.setString(3, message);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        }
    }
    
    public static List<Note> getNotesForPlayer(String playerUuid) throws SQLException {
        String sql = "SELECT id, player_uuid, moderator_uuid, message, created_at FROM notes WHERE player_uuid = ? ORDER BY created_at DESC";
        List<Note> notes = new ArrayList<>();
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, playerUuid);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Note note = new Note(
                        rs.getInt("id"),
                        rs.getString("player_uuid"),
                        rs.getString("moderator_uuid"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at").toInstant()
                    );
                    notes.add(note);
                }
            }
        }
        return notes;
    }
    
    public static boolean deleteNote(int noteId) throws SQLException {
        String sql = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, noteId);
            return stmt.executeUpdate() > 0;
        }
    }
}