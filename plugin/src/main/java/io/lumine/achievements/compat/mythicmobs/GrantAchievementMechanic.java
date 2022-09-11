package io.lumine.achievements.compat.mythicmobs;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;

import org.bukkit.entity.Player;

public class GrantAchievementMechanic implements ITargetedEntitySkill {

    private final MythicAchievementsPlugin plugin;
    private String achievement;
    private boolean giveRewards = false;

    public GrantAchievementMechanic(MythicAchievementsPlugin plugin, MythicLineConfig config) {
        this.plugin = plugin;
        
        this.achievement = config.getString(new String[] {"achievement", "achieve", "a"}, null);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        if(!abstractEntity.isPlayer()) {
            return SkillResult.INVALID_TARGET;
        }

        var maybeAchieve = plugin.getAchievementManager().getAchievement(achievement);
        if(maybeAchieve.isEmpty()) {
            return SkillResult.INVALID_CONFIG;
        }
        
        var achieve = maybeAchieve.get();
        
        var player = (Player) abstractEntity.getBukkitEntity();
        var profile = plugin.getProfiles().getProfile(player);

        profile.completeAchievement(achieve, giveRewards);

        return SkillResult.SUCCESS;
    }
    
    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }
}
