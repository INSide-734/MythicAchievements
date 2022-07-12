package io.lumine.achievements.players;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.storage.players.PlayerRepository;
import io.lumine.mythic.bukkit.utils.storage.players.adapters.file.JsonPlayerStorageAdapter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import java.util.Optional;
import java.util.UUID;

public class ProfileManager extends PlayerRepository<MythicAchievementsPlugin,Profile> {

    public ProfileManager(MythicAchievementsPlugin plugin) {
        super(plugin, Profile.class);
        
        switch(plugin.getConfiguration().getStorageType()) {
            case LUMINE:
                this.initialize(plugin.getCompatibility().getLumineCore().get().getStorageDriver());
                break;
            default:
                this.initialize(new JsonPlayerStorageAdapter<>(plugin,Profile.class));
                break;
        }

    }

    @Override
    public Profile createProfile(UUID id, String name) {
        return new Profile(id,name);
    }

    @Override
    public void initProfile(Profile profile, Player player) {
        profile.initialize(this,player);
        //Events.call(new AchivementPlayerLoadedEvent(player,profile));
    }

    @Override
    public void unloadProfile(Profile profile, Player player) {}

    public void reloadAllAchievements() {
        this.getKnownProfiles().forEach(profile -> profile.rebuildAchievements());
    }

}
