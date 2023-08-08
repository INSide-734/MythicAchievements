package io.lumine.achievements.compat;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.core.LumineCore;
import io.lumine.core.players.PlayerProfile;
import io.lumine.core.utils.gson.GsonProvider;
import io.lumine.core.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.PluginModule;
import io.lumine.mythic.bukkit.utils.promise.Promise;
import io.lumine.mythic.bukkit.utils.storage.players.PlayerStorageAdapter;

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
    
    public class LumineCoreStorageDriver extends PluginModule<MythicAchievementsPlugin> implements PlayerStorageAdapter<ProfileImpl> {

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
        public Promise<Optional<ProfileImpl>> load(UUID uuid, int subProfileOffset) {
            final Promise<Optional<ProfileImpl>> promise = Promise.empty();

            core.getProfiles().getProfile(uuid).thenAcceptAsync(maybeCoreProfile -> {
                if(maybeCoreProfile.isPresent()) {
                    promise.supply(getFromCoreProfile(maybeCoreProfile.get(), subProfileOffset));
                } else {
                    promise.supply(Optional.empty());
                }
            });
            return promise;
        }

        @Override
        public Promise<Optional<ProfileImpl>> loadByName(String name, int subProfileOffset) {
            final Promise<Optional<ProfileImpl>> promise = Promise.empty();

            core.getProfiles().getProfile(name).thenAcceptAsync(maybeCoreProfile -> {
                if(maybeCoreProfile.isPresent()) {
                    promise.supply(getFromCoreProfile(maybeCoreProfile.get(), subProfileOffset));
                } else {
                    promise.supply(Optional.empty());
                }
            });
            return promise;
        }


        @Override
        public Promise save(UUID uuid, ProfileImpl arg1) {
            return Promise.completed(true);
        }

        public boolean saveSync(UUID key, ProfileImpl profile) {
            return true;
        }
    }

    private Optional<ProfileImpl> getFromCoreProfile(PlayerProfile coreProfile, int subProfileOffset) {
        String profileKey = (subProfileOffset == 0) ? "MYTHICACHIEVEMENTS" : "MYTHICACHIEVEMENTS-" + subProfileOffset;

        var maybeProfile = coreProfile.getMetadata(profileKey, ProfileImpl.class);

        if(maybeProfile.isPresent()) {
            return Optional.of(maybeProfile.get());
        } else {
            return Optional.empty();
        }
    }

}
