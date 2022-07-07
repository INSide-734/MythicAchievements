package io.lumine.achievements.players;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile implements AchievementProfile,io.lumine.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    @Getter private transient Player player;
    
    public Profile() {}
    
    public Profile(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public void initialize(final Player player)  {
        this.player = player;
    }

    @Override
    public boolean has(Achievement achievement) {
        return true;
    }


}
