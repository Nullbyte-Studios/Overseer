package com.nullbyte.overseer.util.database;

import com.nullbyte.overseer.Overseer;
import com.nullbyte.overseer.util.database.dao.NoteDao;
import com.nullbyte.overseer.util.database.dao.PlayerDao;
import com.nullbyte.overseer.util.structures.DatabasePlayer;
import com.nullbyte.overseer.util.structures.Note;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jdbi.v3.core.Jdbi;

@UtilityClass
public class DatabaseUtil {
    private final Jdbi jdbi = Overseer.getDatabaseManager().getJdbi();
    public void createPlayer(Player player) {
        jdbi.useExtension(PlayerDao.class, dao -> dao.createPlayer(player.getUniqueId().toString()));
    }
    public DatabasePlayer getPlayer(Player player) {
        return jdbi.withExtension(PlayerDao.class, dao -> dao.getPlayer(player.getUniqueId().toString()));
    }

    public Note createNote(Player player, Player moderator, String message) {
        return jdbi.withExtension(NoteDao.class, dao -> {
            int noteId = dao.createNote(player.getUniqueId().toString(), moderator.getUniqueId().toString(), message);
            return dao.getNote(noteId);
        });
    }
}
