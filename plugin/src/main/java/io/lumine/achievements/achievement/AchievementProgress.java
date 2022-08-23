package io.lumine.achievements.achievement;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.mythic.bukkit.utils.logging.Log;

public class AchievementProgress {

    private final Map<String,Integer> progress = Maps.newConcurrentMap();
    private final Set<String> completed = Sets.newHashSet();
    
    public int incrementProgress(AchievementCriteria criteria, int amount) {
        if(completed.contains(criteria.getKey())) {
            return 0;
        }
        
        int newValue = progress.merge(criteria.getKey(), amount, (o,n) -> o + n);
        Log.info("Amount {0} newAmount {1} Requried {2}", amount, newValue, criteria.getAmount());
        if(newValue >= criteria.getAmount()) {
            completed.add(criteria.getKey());
            return 0;
        }
        return newValue;
    }
    
    public boolean hasCompleted(Achievement achievement) {
        return completed.size() >= achievement.getCriteria().size();
    }
    
}
