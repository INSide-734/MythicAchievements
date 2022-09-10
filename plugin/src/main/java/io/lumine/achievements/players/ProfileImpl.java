package io.lumine.achievements.players;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.AchievementProgress;
import io.lumine.achievements.achievement.CompletedAchievement;
import io.lumine.achievements.api.MythicAchievements;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.constants.Constants;
import io.lumine.achievements.storage.sql.SqlStorage;
import io.lumine.achievements.storage.sql.jooq.Keys;
import io.lumine.achievements.storage.sql.jooq.tables.records.ProfileRecord;
import io.lumine.mythic.bukkit.utils.logging.Log;
import lombok.Getter;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class ProfileImpl implements AchievementProfile,io.lumine.mythic.bukkit.utils.storage.players.Profile {

    @Getter private UUID uniqueId;
    @Getter private String name;
    @Getter private long timestamp = System.currentTimeMillis();

    private Map<String,AchievementProgress> achievementProgress = Maps.newConcurrentMap();
    private Map<String,CompletedAchievement> completedAchievements = Maps.newConcurrentMap();
    
    @Getter private transient Collection<Achievement> subscribedAchievements = Sets.newHashSet();
    
    @Getter private transient ProfileManager manager;
    @Getter private transient Player player;
    
    public ProfileImpl() {}
    
    public ProfileImpl(UUID id, String name) {
        this.uniqueId = id;
        this.name = name;
    }
    
    public ProfileImpl(UUID id, String name, long timestamp) {
        this.uniqueId = id;
        this.name = name;
        this.timestamp = timestamp;
    }
    
    public void loadFromSql(ProfileRecord profileRecord) {
        var fetchProgress = profileRecord.fetchChildren(Keys.MYTHICACHIEVEMENTS_PROFILE_PROGRESS_UUID_FK);
        
        for(var result : fetchProgress) {
            var achieve = result.getAchievement();
            var criteria = result.getCriteria();
            var progress = result.getProgress();

            var progressEntry = achievementProgress.getOrDefault(achieve, null);
            
            if(progressEntry == null) {
                progressEntry = new AchievementProgress();
                achievementProgress.put(achieve, progressEntry);
            }
            progressEntry.setCriteriaProgress(criteria, progress);
        }
        
        var fetchCompleted = profileRecord.fetchChildren(Keys.MYTHICACHIEVEMENTS_PROFILE_COMPLETED_FK);
        for(var result : fetchCompleted) {
            var achieve = result.getAchievement();
            completedAchievements.put(achieve, new CompletedAchievement(result));
        }
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
    
    public Collection<String> getCompletedAchievementNames() {
        return this.completedAchievements.keySet();
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
    
    public void incrementAchievementStat(Achievement achievement, AchievementCriteria criteria, int amount) {
        var progress = achievementProgress.getOrDefault(achievement.getKey(), null);
        
        if(progress == null) {
            progress = new AchievementProgress();
            achievementProgress.put(achievement.getKey(), progress);
        }
        
        if(progress.incrementProgress(this, achievement, criteria, amount)) {
            completeAchievement(achievement, true);
        }
    }
    
    public void completeAchievement(Achievement achieve, boolean giveRewards) {
        final var completed = new CompletedAchievement(achieve);
        
        achievementProgress.remove(achieve.getKey());
        completedAchievements.put(achieve.getKey(), completed);
        unsubscribeFromAchievement(achieve);
        
        achieve.sendCompletedMessage(player);
        
        if(giveRewards) {
            achieve.giveRewards(player);
        }
        
        if(manager.getAdapter() instanceof SqlStorage sqlStorage) {
            sqlStorage.saveCompletedAchievement(this, achieve.getKey(), completed);
        }
        
        subscribedOrCompleted(achieve);
    }
    
    public void revokeAchievement(Achievement achieve) {
        achievementProgress.remove(achieve.getKey());
        completedAchievements.remove(achieve.getKey());

        var adv = achieve.getAdvancement();
        
        if(adv != null) {
            final var progress = player.getAdvancementProgress(adv);
            progress.revokeCriteria(Constants.CRITERIA_KEY);
        }
        
        rebuildAchievements();
    }
    
    public void resetAchievements() {
        for(var key : achievementProgress.keySet()) {
            var achieve = manager.getPlugin().getAchievementManager().getAchievement(key);
            
            if(achieve.isEmpty()) {
                continue;
            }
            
            var adv = achieve.get().getAdvancement();
            
            if(adv != null) {
                final var progress = player.getAdvancementProgress(adv);
                progress.revokeCriteria(Constants.CRITERIA_KEY);
            }
        }

        for(var key : completedAchievements.keySet()) {
            var achieve = manager.getPlugin().getAchievementManager().getAchievement(key);
            
            if(achieve.isEmpty()) {
                continue;
            }
            
            var adv = achieve.get().getAdvancement();
            
            if(adv != null) {
                final var progress = player.getAdvancementProgress(adv);
                progress.revokeCriteria(Constants.CRITERIA_KEY);
            }
        }
        
        achievementProgress.clear();
        completedAchievements.clear();
        
        rebuildAchievements();
    }
    
    public void subscribeToAchievement(Achievement achieve) {
        //Log.info("Subscribed to achievement {0}", achieve.getKey());
        achieve.subscribe(this);
        subscribedAchievements.add(achieve);
    }
    
    public void unsubscribeFromAchievement(Achievement achieve) {
        achieve.unsubscribe(this);
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
            achieve.unsubscribe(this);
        }
        subscribedAchievements.clear();
        
        for(var cat : manager.getPlugin().getAchievementManager().getCategories()) {
            if(!cat.isHidden()) {
                var adv = cat.getAdvancement();
                
                if(adv != null) {
                    final var progress = player.getAdvancementProgress(adv);
                    progress.awardCriteria(Constants.CRITERIA_KEY);
                }
            }
            for(var achieve : cat.getBaseAchievements()) {
                subscribedOrCompleted(achieve);
            }
        }
        //Log.info("Subscribed to {0} achievements", subscribedAchievements.size());
    }
    
    private void subscribedOrCompleted(Achievement achieve) {
        //Log.info("SubComp achievement {0}", achieve.getKey());
        if(hasCompleted(achieve)) {
            //Log.info("Completing achievement {0}", achieve.getKey());
            var adv = achieve.getAdvancement();
            
            if(adv != null) {
                //Log.info("Completing achievement progress {0}", achieve.getKey());
                final var progress = player.getAdvancementProgress(adv);
                progress.awardCriteria(Constants.CRITERIA_KEY);
            }
            
            for(var child : achieve.getChildren()) {
                subscribedOrCompleted(child);
            }
        } else if(isProgressable(achieve)) {
            //Log.info("---- Can sub to achievement {0}", achieve.getKey());
            subscribeToAchievement(achieve);
        }
    }

}
