package io.lumine.achievements.compat;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.players.Profile;
import io.lumine.core.LumineCore;
import io.lumine.core.players.PlayerProfile;
import io.lumine.core.utils.gson.GsonProvider;
import io.lumine.core.utils.logging.Log;
import io.lumine.utils.plugin.PluginModule;
import io.lumine.utils.promise.Promise;
import io.lumine.utils.storage.players.PlayerStorageAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class LumineCoreCompat {

    private final MythicAchievementsPlugin plugin;
    
    public LumineCoreCompat(MythicAchievementsPlugin plugin) {
        this.plugin = plugin;
    }
    
    public LumineCoreStorageDriver getStorageDriver() {
        return new LumineCoreStorageDriver(plugin);
    }
    
    public class LumineCoreStorageDriver extends PluginModule<MythicAchievementsPlugin> implements PlayerStorageAdapter<Profile> {

        private final LumineCore core;
        
        public LumineCoreStorageDriver(MythicAchievementsPlugin plugin) {
            super(plugin, false);
            this.core = LumineCore.inst();
            
            load(plugin);
        }

        @Override
        public void load(MythicAchievementsPlugin plugin) {}

        @Override
        public void unload() {}
        
        @Override
        public Promise<Optional<Profile>> load(UUID uuid) {
            final Promise<Optional<Profile>> promise = Promise.empty();

            core.getProfiles().getProfile(uuid).thenAcceptAsync(maybeCoreProfile -> {
                if(maybeCoreProfile.isPresent()) {
                    promise.supply(getFromCoreProfile(maybeCoreProfile.get()));
                } else {
                    promise.supply(Optional.empty());
                }
            });
            return promise;
        }

        @Override
        public Promise<Optional<Profile>> loadByName(String name) {
            final Promise<Optional<Profile>> promise = Promise.empty();

            core.getProfiles().getProfile(name).thenAcceptAsync(maybeCoreProfile -> {
                if(maybeCoreProfile.isPresent()) {
                    promise.supply(getFromCoreProfile(maybeCoreProfile.get()));
                } else {
                    promise.supply(Optional.empty());
                }
            });
            return promise;
        }


        @Override
        public Promise save(UUID uuid, Profile arg1) {
            return Promise.completed(true);
        }

        public boolean saveSync(UUID key, Profile profile) {
            File file = getFile(key.toString());

            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return false;
            }

        try(
        FileWriter writer = new FileWriter(file)) {
            GsonProvider.standard().toJson(profile, writer);
            return true;
        } catch(IOException e) {
            Log.severe("Failed to save Json Profile in: " + this.getClass().getName());
            e.printStackTrace();
        }
        return false;

    }

    protected File getFile(String key) {
        return new File(plugin.getDataFolder() + "/data", key + ".json");
    }


    private Optional<Profile> getFromCoreProfile(PlayerProfile coreProfile) {
            var maybeProfile = coreProfile.getMetadata("MCCOSMETICS", Profile.class);
            
            if(maybeProfile.isPresent()) {
                return Optional.of(maybeProfile.get());
            } else {
                return Optional.empty();
            }
        }


    }
    
}
