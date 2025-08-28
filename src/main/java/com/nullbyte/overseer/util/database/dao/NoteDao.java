package com.nullbyte.overseer.util.database.dao;

import com.nullbyte.overseer.util.structures.Note;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface NoteDao {
    @SqlUpdate("INSERT INTO notes (player_uuid, moderator_uuid, message) VALUES (:playerUuid, :moderatorUuid, :message)")
    @GetGeneratedKeys
    int createNote(@Bind("playerUuid") String playerUuid,
                    @Bind("moderatorUuid") String moderatorUuid,
                    @Bind("message") String message);

    @SqlQuery("SELECT * FROM notes WHERE player_uuid = :playerUuid ORDER BY created_at DESC")
    List<Note> getNotes(@Bind("playerUuid") String playerUuid);

    @SqlQuery("SELECT * FROM notes WHERE id = :id")
    Note getNote(@Bind("id") int id);

    @SqlUpdate("DELETE FROM notes WHERE id = :id")
    void deleteNoteById(@Bind("id") int id);

}
