package io.lumine.achievements.compat.mythicmobs;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import org.bukkit.entity.Player;

public class HasAchievementCondition implements IEntityCondition {

    private final MythicAchievementsPlugin plugin;
    private String achievement;
    
    public HasAchievementCondition(MythicAchievementsPlugin plugin, MythicLineConfig config) {
        this.plugin = plugin;
        
        this.achievement = config.getString(new String[] {"achievement", "achieve", "a"}, null);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        if(!abstractEntity.isPlayer()) {
            return false;
        }

        var maybeAchieve = plugin.getAchievementManager().getAchievement(achievement);
        if(maybeAchieve.isEmpty()) {
            return false;
        }
        
        var achieve = maybeAchieve.get();
        
        var player = (Player) abstractEntity.getBukkitEntity();
        var profile = plugin.getProfiles().getProfile(player);

        return profile.hasCompleted(achieve);
    }
}
