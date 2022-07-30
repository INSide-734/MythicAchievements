package io.lumine.achievements.players;

import io.lumine.achievements.achievement.AchievementImpl;
import io.lumine.achievements.achievement.CompletedAchievement;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.constants.Constants;
import io.lumine.mythic.bukkit.utils.logging.Log;
import lombok.Getter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Profile implements AchievementProfile,io.lumine.mythic.bukkit.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    private Map<String,Integer> achievementProgress = Maps.newConcurrentMap();
    private Map<String,CompletedAchievement> completedAchievements = Maps.newConcurrentMap();
    
    private transient Collection<Achievement> subscribedAchievements = Sets.newHashSet();
    
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
    public boolean hasCompleted(Achievement achieve) {
        return this.completedAchievements.containsKey(achieve.getKey());
    }

    @Override
    public boolean hasCollectedReward(Achievement achieve) {
        var completed = this.completedAchievements.get(achieve.getKey());
        
        if(completed == null) {
            return false;
        } else {
            return completed.isRewardClaimed();
        }
    }
    
    @Override
    public void setRewardsCollected(Achievement achieve) {
        var completed = this.completedAchievements.get(achieve.getKey());
        
        if(completed != null) {
            completed.setRewardClaimed(true);
        }
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
        completedAchievements.put(achieve.getKey(), new CompletedAchievement(achieve));
        unsubscribeFromAchievement(achieve);
        
        achieve.sendCompletedMessage(player);
        
        if(giveRewards) {
            achieve.giveRewards(player);
        }
        
        for(var child : achieve.getChildren()) {
            subscribeToAchievement(child);
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
        Log.info("Subscribed to achievement {0}", achieve.getKey());
        ((AchievementImpl) achieve).getSubscribedPlayers().put(uniqueId,this);
        subscribedAchievements.add(achieve);
    }
    
    public void unsubscribeFromAchievement(Achievement achieve) {
        ((AchievementImpl) achieve).getSubscribedPlayers().remove(uniqueId);
        subscribedAchievements.remove(achieve);
    }
    
    public boolean isProgressable(Achievement achieve) {
        if(completedAchievements.containsKey(achieve.getKey())) {
            Log.info("-- Achievement already completed");
            return false;
        }
        if(achieve.hasParent()) {
            var parent = achieve.getParent().get();
            
            if(!completedAchievements.containsKey(parent.getKey())) {
                Log.info("-- Achievement parent not completed");
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
        
        for(var cat : manager.getPlugin().getAchievementManager().getCategories()) {
            if(!cat.isHidden()) {
                var adv = cat.getAdvancement();
                
                if(adv != null) {
                    final var progress = player.getAdvancementProgress(adv);
                    
                    if(!progress.getRemainingCriteria().isEmpty()) {
                        progress.awardCriteria(Constants.CRITERIA_KEY);
                    }
                }
            }
            for(var achieve : cat.getAchievements()) {
                Log.info("-- Checking achievement {0}", achieve.getKey());
                subscribedOrCompleted(achieve);
            }
        }
        Log.info("Subscribed to {0} achievements", subscribedAchievements.size());
    }
    
    private void subscribedOrCompleted(Achievement achieve) {
        if(hasCompleted(achieve)) {
            var adv = achieve.getAdvancement();
            
            if(adv != null) {
                final var progress = player.getAdvancementProgress(adv);
                
                if(!progress.getRemainingCriteria().isEmpty()) {
                    progress.awardCriteria(Constants.CRITERIA_KEY);
                }
            }
            
            for(var child : achieve.getChildren()) {
                subscribedOrCompleted(child);
            }
        } else if(isProgressable(achieve)) {
            Log.info("---- Can sub to achievement {0}", achieve.getKey());
            subscribeToAchievement(achieve);
        }
    }

}
