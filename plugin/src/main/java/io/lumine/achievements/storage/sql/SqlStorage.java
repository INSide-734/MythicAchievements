package io.lumine.achievements.storage.sql;

import java.util.Optional;
import java.util.UUID;

import io.lumine.achievements.achievement.CompletedAchievement;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.achievements.players.ProfileManager;
import io.lumine.achievements.storage.sql.jooq.DefaultSchema;
import io.lumine.achievements.storage.sql.jooq.Keys;
import io.lumine.achievements.storage.sql.jooq.Tables;
import io.lumine.mythic.bukkit.utils.lib.jooq.exception.DataAccessException;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.LuminePlugin;
import io.lumine.mythic.bukkit.utils.promise.Promise;
import io.lumine.mythic.bukkit.utils.storage.players.Profile;
import io.lumine.mythic.bukkit.utils.storage.players.adapters.SqlPlayerStorageAdapter;
import io.lumine.mythic.bukkit.utils.storage.sql.SqlConnector;

public class SqlStorage<MythicAchievements,ProfileImpl> extends SqlPlayerStorageAdapter {

    private final ProfileManager manager;
    
    public SqlStorage(ProfileManager manager, SqlConnector connector) {
        super(manager.getPlugin(), connector);
        this.manager = manager;
        
        load(manager.getPlugin());
    }

    @Override
    public void load(LuminePlugin plugin) {
        var connection = getConnector().open();

        try {
            connection.fetch(Tables.MYTHICACHIEVEMENTS_PROFILE);
        } catch(DataAccessException ex) {
            Log.info("Generating database schema...");
            connection.ddl(DefaultSchema.DEFAULT_SCHEMA).executeBatch();
        }
    }

    @Override
    public void unload() {
    }

    @Override
    public Promise<Optional<io.lumine.achievements.players.ProfileImpl>> load(final UUID uuid) {
        return Promise.supplyingDelayedAsync(() -> {
            var connection = getConnector().open();

            var fetchProfile = connection.selectFrom(Tables.MYTHICACHIEVEMENTS_PROFILE)
                .where(Tables.MYTHICACHIEVEMENTS_PROFILE.UUID.eq(uuid.toString()))
                .fetchAny();
            
            if(fetchProfile == null) {
                return Optional.empty();
            }
            
            var profile = manager.createProfile(UUID.fromString(fetchProfile.getUuid()), fetchProfile.getName());

            profile.loadFromSql(fetchProfile);
            
            return Optional.ofNullable(profile);
        }, 20);
    }

    @Override
    public Promise<Optional<io.lumine.achievements.players.ProfileImpl>> loadByName(String name) {
        return Promise.supplyingAsync(() -> {
            var connection = getConnector().open();

            var fetchProfile = connection.selectFrom(Tables.MYTHICACHIEVEMENTS_PROFILE)
                .where(Tables.MYTHICACHIEVEMENTS_PROFILE.NAME.eq(name))
                .fetchAny();
            
            if(fetchProfile == null) {
                return Optional.empty();
            }
            
            var profile = manager.createProfile(UUID.fromString(fetchProfile.getUuid()), fetchProfile.getName());

            profile.loadFromSql(fetchProfile);
            
            return Optional.ofNullable(profile);
        });
    }

    @Override
    public Promise<Boolean> save(UUID uuid, Profile profile) {
        return Promise.supplyingAsync(() -> {
            return saveSync(uuid,profile);
        });
    }

    @Override
    public boolean saveSync(UUID uuid, Profile profile) {
        var connection = getConnector().open();

        connection.insertInto(Tables.MYTHICACHIEVEMENTS_PROFILE, 
                                Tables.MYTHICACHIEVEMENTS_PROFILE.UUID, 
                                Tables.MYTHICACHIEVEMENTS_PROFILE.NAME)
                .values(uuid.toString(), profile.getName())
                .onDuplicateKeyUpdate()
                .set(Tables.MYTHICACHIEVEMENTS_PROFILE.NAME, profile.getName())
                .returning()
                .execute();

        return true; 
    }

    public Promise<Boolean> saveCriteriaProgress(io.lumine.achievements.players.ProfileImpl profile, 
            String achieve, String criteria, int progress) {
        
        return Promise.supplyingAsync(() -> {
            var connection = getConnector().open();

            connection.insertInto(Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS, 
                    Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.PROFILE_UUID,
                    Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.ACHIEVEMENT,
                    Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.CRITERIA,
                    Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.PROGRESS)
            .values(profile.getUniqueId().toString(), achieve, criteria, progress)
            .onDuplicateKeyUpdate()
            .set(Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.PROGRESS, progress)
            .execute();

            return true;
        });
    }
    
    public Promise<Boolean> saveCompletedAchievement(io.lumine.achievements.players.ProfileImpl profile, String achieve, CompletedAchievement data) {
        return Promise.supplyingAsync(() -> {
            var connection = getConnector().open();

            connection.insertInto(Tables.MYTHICACHIEVEMENTS_PROFILE_COMPLETED, 
                        Tables.MYTHICACHIEVEMENTS_PROFILE_COMPLETED.PROFILE_UUID,
                        Tables.MYTHICACHIEVEMENTS_PROFILE_COMPLETED.ACHIEVEMENT,
                        Tables.MYTHICACHIEVEMENTS_PROFILE_COMPLETED.COMPLETED_TIME,
                        Tables.MYTHICACHIEVEMENTS_PROFILE_COMPLETED.COLLECTED_LOOT)
                .values(profile.getUniqueId().toString(), achieve, data.getTimestamp(), data.isRewardClaimed() ? (byte) 1 : (byte) 0)
                .onDuplicateKeyUpdate()
                .set(Tables.MYTHICACHIEVEMENTS_PROFILE_COMPLETED.COLLECTED_LOOT, data.isRewardClaimed() ? (byte) 1 : (byte) 0)
                .execute();
            
            connection.deleteFrom(Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS)
                .where(
                        Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.PROFILE_UUID.eq(profile.getUniqueId().toString()),
                        Tables.MYTHICACHIEVEMENTS_PROFILE_PROGRESS.ACHIEVEMENT.eq(achieve))
                .execute();

            return true;
        });
    }

}
