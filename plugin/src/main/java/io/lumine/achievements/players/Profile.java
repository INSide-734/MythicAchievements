package io.lumine.achievements.players;

import io.lumine.achievements.achievement.AchievementImpl;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.logging.Log;
import lombok.Getter;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class Profile implements AchievementProfile,io.lumine.mythic.bukkit.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    private Map<String,Integer> achievementProgress = Maps.newConcurrentMap();
    private Map<String,Long> completedAchievements = Maps.newConcurrentMap();
    
    private Collection<Achievement> subscribedAchievements = Sets.newHashSet();
    
    @Getter private transient ProfileManager manager;
    @Getter private transient Player player;
    
    public Profile() {}
    
    public Profile(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public void initialize(ProfileManager manager, final Player player)  {
        this.manager = manager;
        this.player = player;
        
        rebuildAchievements();
    }

    @Override
    public boolean has(Achievement achievement) {
        return true;
    }

    public void incrementAchievementStat(Achievement achievement, int amount) {
        var newValue = achievementProgress.merge(achievement.getKey(), amount, (o,n) -> o + n);
        
        if(newValue >= ((AchievementImpl) achievement).getCriteria().getAmount()) {
            completeAchievement(achievement, true);
        }
    }
    
    public void completeAchievement(Achievement achieve, boolean giveRewards) {
        if(!achievementProgress.containsKey(achieve.getKey())) {
            return;
        }
        achievementProgress.remove(achieve.getKey());
        completedAchievements.put(achieve.getKey(), System.currentTimeMillis());
        
        achieve.sendCompletedMessage(player);
        
        if(giveRewards) {
            achieve.giveRewards(player);
        }
    }
    
    public void resetAchievement(Achievement achieve) {
        achievementProgress.remove(achieve.getKey());
        completedAchievements.remove(achieve.getKey());
        
        if(isProgressable(achieve)) {
            subscribeToAchievement(achieve);
        }
    }
    
    public void subscribeToAchievement(Achievement achieve) {
        ((AchievementImpl) achieve).getSubscribedPlayers().put(uniqueId,this);
        subscribedAchievements.add(achieve);
    }
    
    public void unsubscribeFromAchievement(Achievement achieve) {
        ((AchievementImpl) achieve).getSubscribedPlayers().remove(uniqueId);
        subscribedAchievements.remove(achieve);
    }
    
    public boolean isProgressable(Achievement achieve) {
        if(completedAchievements.containsKey(achieve.getKey())) {
            return false;
        }
        if(achieve.hasParent()) {
            var parent = achieve.getParent().get();
            
            if(!completedAchievements.containsKey(parent.getKey())) {
                return false;
            }
        }
        return true;
    }
    
    public void rebuildAchievements() {
        for(var achieve : subscribedAchievements) {
            ((AchievementImpl) achieve).getSubscribedPlayers().remove(uniqueId);
        }
        
        subscribedAchievements.clear();
        
        for(var achieve : manager.getPlugin().getAchievementManager().getAchievements()) {
            if(isProgressable(achieve)) {
                subscribeToAchievement(achieve);
            }
        }
        Log.info("Subscribed to {0} achievements", subscribedAchievements.size());
    }

}
