package io.lumine.achievements.storage;

import io.lumine.mythic.bukkit.utils.storage.sql.SqlProvider;
import lombok.Getter;

public enum StorageDriver {
    JSON(false),
    MYSQL(true, SqlProvider.MYSQL),
    MARIADB(true, SqlProvider.MARIADB),
    LUMINE(false)
    
    ;
    
    @Getter private final boolean sql;
    @Getter private final SqlProvider sqlProvider;
    
    private StorageDriver(boolean sql) {
        this.sql = sql;
        this.sqlProvider = null;
    }
    
    private StorageDriver(boolean sql, SqlProvider provider) {
        this.sql = sql;
        this.sqlProvider = provider;
    }
}
