package io.lumine.achievements.api.achievements;

import java.io.File;
import java.util.Optional;

import org.bukkit.entity.Player;

import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.menu.MenuData;
import lombok.Getter;

public abstract class Achievement implements PropertyHolder,MenuData<AchievementProfile> {
 
    @Getter private final AchievementManager manager;
    @Getter private final String key;

    public Achievement(AchievementManager manager, String key) {
        this.manager = manager;
        this.key = key;
    }

    public abstract File getFile();
    
    public boolean has(AchievementProfile profile) {
        return profile.has(this);
    }
    
    public abstract AchievementCriteria getCriteria();
    
    public abstract Optional<Achievement> getParent();
    
    public boolean hasParent() {
        return getParent().isPresent();
    }
    
    public void incrementIfSubscribed(Player player) {
        incrementIfSubscribed(player, 1);
    }
    
    public abstract void incrementIfSubscribed(Player player, int amount);
    
    public abstract void sendCompletedMessage(Player player);
    
    public abstract void giveRewards(Player player);
    
}
