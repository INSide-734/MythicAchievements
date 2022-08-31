package io.lumine.achievements.achievement;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.achievements.storage.sql.SqlStorage;
import io.lumine.achievements.storage.sql.jooq.tables.MythicachievementsProfileProgress;
import io.lumine.mythic.bukkit.utils.logging.Log;

public class AchievementProgress {

    private final Map<String,Integer> progress = Maps.newConcurrentMap();
    private final Set<String> completed = Sets.newHashSet();

    public void setCriteriaProgress(String criteria, int progress) {
        if(progress == -1) {
            this.completed.add(criteria);
        } else {
            this.progress.put(criteria, progress);
        }
    }
    
    public boolean incrementProgress(ProfileImpl profile, Achievement achievement, AchievementCriteria criteria, int amount) {
        if(completed.contains(criteria.getKey())) {
            if(hasCompleted(achievement)) {
                return true;
            } else {
                return false;
            }
        }
        
        int newValue = progress.merge(criteria.getKey(), amount, (o,n) -> o + n);
        
        //Log.info("Amount {0} newAmount {1} Required {2}", amount, newValue, criteria.getAmount());
        if(newValue >= criteria.getAmount()) {
            completed.add(criteria.getKey());
            
            if(hasCompleted(achievement)) {
                return true;
            } else {
                if(profile.getManager().getAdapter() instanceof SqlStorage sqlStorage) {
                    sqlStorage.saveCriteriaProgress(profile, achievement.getKey(), criteria.getKey(), -1);
                }
                return false;
            }
        } else {
            if(profile.getManager().getAdapter() instanceof SqlStorage sqlStorage) {
                sqlStorage.saveCriteriaProgress(profile, achievement.getKey(), criteria.getKey(), newValue);
            }
        }
        return false;
    }
    
    public boolean hasCompleted(Achievement achievement) {
        return completed.size() >= achievement.getCriteria().size();
    }
    
}
