package io.lumine.achievements.api.achievements.manager;

import java.util.Optional;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementCriteria;

public interface AchievementManager {

    public Optional<Achievement> getAchievement(String name);
    
    public Optional<AchievementCategory> getCategory(String name);
    
    public Optional<AchievementCriteria> getCriteria(Achievement achievement, String criteriaType);
    
}
