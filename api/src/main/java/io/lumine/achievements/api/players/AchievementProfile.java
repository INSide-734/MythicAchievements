package io.lumine.achievements.api.players;

import io.lumine.achievements.api.achievements.Achievement;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface AchievementProfile {

    public UUID getUniqueId();
    
    public String getName();
    
    public Player getPlayer();
    
    public boolean has(Achievement achievement);

}
