package io.lumine.achievements.storage.sql;

import java.util.UUID;

import io.lumine.mythic.bukkit.utils.promise.Promise;
import io.lumine.mythic.bukkit.utils.storage.players.Profile;
import io.lumine.mythic.bukkit.utils.storage.players.adapters.SqlPlayerStorageAdapter;

public class SqlStorage implements SqlPlayerStorageAdapter {

    @Override
    public Promise load(UUID uuid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Promise loadByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Promise save(UUID uuid, Profile profile) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean saveSync(UUID uuid, Profile profile) {
        // TODO Auto-generated method stub
        return false;
    }

}
