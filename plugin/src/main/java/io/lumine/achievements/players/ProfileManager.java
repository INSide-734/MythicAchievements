package io.lumine.achievements.players;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.storage.sql.SqlStorage;
import io.lumine.mythic.bukkit.utils.storage.players.PlayerRepository;
import io.lumine.mythic.bukkit.utils.storage.players.adapters.file.JsonPlayerStorageAdapter;
import io.lumine.mythic.bukkit.utils.storage.sql.SqlConnector;

import org.bukkit.entity.Player;
import java.util.UUID;

public class ProfileManager extends PlayerRepository<MythicAchievementsPlugin,ProfileImpl> {

    public ProfileManager(MythicAchievementsPlugin plugin) {
        super(plugin, ProfileImpl.class);
        
        if(plugin.getConfiguration().getStorageType().isSql()) {
            
            final var provider = plugin.getConfiguration().getStorageType().getSqlProvider();
            final var credentials = plugin.getConfiguration().getSqlCredentials();
            
            final var connector = new SqlConnector(plugin, provider, credentials);
            
            this.initialize(new SqlStorage<>(this,connector));
        } else {
            switch(plugin.getConfiguration().getStorageType()) {
                case LUMINE:
                    this.initialize(plugin.getCompatibility().getLumineCore().get().getStorageDriver());
                    break;
                default:
                    this.initialize(new JsonPlayerStorageAdapter<>(plugin,ProfileImpl.class));
                    break;
            }
        }
    }

    @Override
    public ProfileImpl createProfile(UUID id, String name, int subProfileOffset) {
        return new ProfileImpl(id,name);
    }

    @Override
    public void initProfile(ProfileImpl profile, Player player) {
        profile.initialize(this,player);
        //Events.call(new AchivementPlayerLoadedEvent(player,profile));
    }

    @Override
    public void unloadProfile(ProfileImpl profile, Player player) {}

    public void reloadAllAchievements() {
        this.getKnownProfiles().forEach(profile -> profile.rebuildAchievements());
    }

}
