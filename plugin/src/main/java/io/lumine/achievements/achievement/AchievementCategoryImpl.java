package io.lumine.achievements.achievement;

import java.util.List;

import org.bukkit.Material;

import com.google.common.collect.Lists;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.menu.Icon;
import io.lumine.mythic.bukkit.utils.menu.IconBuilder;
import lombok.Getter;

public class AchievementCategoryImpl implements AchievementCategory {

    @Getter private final AchievementsExecutor manager;
    @Getter private final String key;
    
    public List<Achievement> achievements = Lists.newArrayList();
    
    public AchievementCategoryImpl(AchievementsExecutor manager, String key) {
        this.manager = manager;
        this.key = key;
    }
    
    @Override
    public String getPropertyNode() {
        return key;
    }

    @Override
    public Icon<AchievementProfile> getIcon() {
        return IconBuilder.<AchievementProfile>create()
                .material(Material.NETHER_STAR)
                .name(key)
                .click((prof,player) -> {
                
                    manager.getPlugin().getMenuManager().getAchievementsMenu().openMenu(player, this);
                    
                }).build();
    }
    
    public List<Achievement> getAchievements() {
        return achievements;
    }
    
    public void addAchievement(Achievement achieve) {
        this.achievements.add(achieve);
    }

}
