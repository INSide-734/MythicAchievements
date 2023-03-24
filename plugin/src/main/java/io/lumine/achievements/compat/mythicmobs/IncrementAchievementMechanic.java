package io.lumine.achievements.compat.mythicmobs;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.achievement.criteria.ManualCriteria;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.ThreadSafetyLevel;

import org.bukkit.entity.Player;

public class IncrementAchievementMechanic implements ITargetedEntitySkill {

    private final MythicAchievementsPlugin plugin;
    private String achievement;
    private String criteria;
    private int amount;
    
    public IncrementAchievementMechanic(MythicAchievementsPlugin plugin, MythicLineConfig config) {
        this.plugin = plugin;
        
        this.achievement = config.getString(new String[] {"achievement", "achieve", "a"}, null);
        this.criteria = config.getString(new String[] {"criteria", "crit", "c"}, null);
        this.amount = config.getInteger(new String[] {"amount"}, 1);
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

        AchievementCriteria found = null;
        for(var criteria : achieve.getCriteria()) {
            if(criteria.getKey().equalsIgnoreCase(this.criteria)) {
                found = criteria;
                break;
            }
        }
        if(found == null) {
            return SkillResult.INVALID_CONFIG;
        }

        if(found instanceof ManualCriteria manualCriteria) {
            if(manualCriteria.checkConditions(player)) {
                manualCriteria.incrementStat(player);
            }
        } else {
            ((Criteria) found).incrementStat(player);
        }

        return SkillResult.SUCCESS;
    }
    
    @Override
    public ThreadSafetyLevel getThreadSafetyLevel() {
        return ThreadSafetyLevel.SYNC_ONLY;
    }
}
