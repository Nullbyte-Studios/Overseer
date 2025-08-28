package com.nullbyte.overseer.util.database.dao;

import com.nullbyte.overseer.util.structures.DatabasePlayer;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface PlayerDao {
    @SqlUpdate("INSERT OR IGNORE INTO players (uuid) values (:uuid)")
    void createPlayer(@Bind("uuid") String uuid);

    @SqlQuery("SELECT * FROM players WHERE uuid = :uuid")
    DatabasePlayer getPlayer(@Bind("uuid") String uuid);
}
