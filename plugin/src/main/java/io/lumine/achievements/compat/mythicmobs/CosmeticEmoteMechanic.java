package io.lumine.achievements.compat.mythicmobs;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillMechanic;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CosmeticEmoteMechanic extends SkillMechanic implements ITargetedEntitySkill {

    protected String gesture;

    public CosmeticEmoteMechanic(String mechanicName, MythicLineConfig config) {
        super(MythicBukkit.inst().getSkillManager(), mechanicName, config);

        this.gesture = config.getString(new String[]{"gesture", "emote"}, null);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        if(!abstractEntity.isPlayer()){
            return SkillResult.INVALID_TARGET;
        }

        
        return SkillResult.SUCCESS;
    }
}
