package io.lumine.achievements.api.achievements;

import java.util.List;

import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.menu.MenuData;

public interface AchievementCategory extends PropertyHolder,MenuData<AchievementProfile> {

    public void addAchievement(Achievement achieve);
    
    public List<Achievement> getAchievements();
    
}
