package io.lumine.achievements.api.achievements;

import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.menu.MenuData;

public interface AchievementCriteria extends PropertyHolder,MenuData<AchievementProfile> {

    public String getKey();
    
    public void loadListeners();
    
    public void unloadListeners();
    
    public int getAmount();
    
}
