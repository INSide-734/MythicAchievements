package io.lumine.achievements.api.achievements;

import java.util.List;

import org.bukkit.NamespacedKey;

import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.menu.MenuData;

public interface AchievementCategory extends AchievementNode,PropertyHolder,MenuData<AchievementProfile> {

    public NamespacedKey getNamespacedKey();
   
    public String getBackground();
    
    public boolean isHidden();
    
    public void addAchievement(Achievement achieve);
    
    public List<Achievement> getAchievements();

    public List<Achievement> getBaseAchievements();
    
}
