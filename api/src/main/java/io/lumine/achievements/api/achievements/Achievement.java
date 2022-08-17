package io.lumine.achievements.api.achievements;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.menu.MenuData;
import lombok.Getter;

public abstract class Achievement implements AchievementNode,PropertyHolder {
 
    @Getter private final AchievementManager manager;
    @Getter private final String key;
    
    public Achievement(AchievementManager manager, String key) {
        this.manager = manager;
        this.key = key;
    }

    public abstract File getFile();
    
    public abstract NamespacedKey getNamespacedKey();
    
    public boolean hasCompleted(AchievementProfile profile) {
        return profile.hasCompleted(this);
    }
    
    public abstract AchievementCategory getCategory();
    
    public abstract Collection<AchievementCriteria> getCriteria();
    
    public abstract Optional<Achievement> getParent();
    
    public abstract Collection<Achievement> getChildren();
    
    public abstract Material getIconMaterial();
    
    public abstract int getIconModel();
    
    public boolean hasParent() {
        return getParent().isPresent();
    }
    
    public abstract void subscribe(AchievementProfile player);
    
    public abstract void unsubscribe(AchievementProfile player);
    
    public abstract Collection<AchievementProfile> getSubscribedPlayers();
    
    public void incrementIfSubscribed(Player player, AchievementCriteria criteria) {
        incrementIfSubscribed(player, criteria, 1);
    }
    
    public abstract void incrementIfSubscribed(Player player, AchievementCriteria criteria, int amount);
    
    public abstract void sendCompletedMessage(Player player);
    
    public abstract void giveRewards(Player player);
    
}
