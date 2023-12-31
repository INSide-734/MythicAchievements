package io.lumine.achievements.api.players;

import io.lumine.achievements.api.achievements.Achievement;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public interface AchievementProfile {

    public UUID getUniqueId();
    
    public String getName();
    
    public Player getPlayer();
    
    public Collection<Achievement> getSubscribedAchievements();
    
    public boolean hasCompleted(Achievement achievement);
    
    public boolean hasCollectedReward(Achievement achieve);
    
    public void setRewardsCollected(Achievement achieve);

}
