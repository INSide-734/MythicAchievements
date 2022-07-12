package io.lumine.achievements.achievement;

import java.util.Collection;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringListProp;
import io.lumine.mythic.bukkit.utils.terminable.Terminable;
import io.lumine.mythic.bukkit.utils.terminable.TerminableConsumer;
import io.lumine.mythic.bukkit.utils.terminable.TerminableRegistry;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.core.skills.SkillCondition;
import lombok.Getter;

public abstract class Criteria implements AchievementCriteria,Terminable,TerminableConsumer {

    private final IntProp AMOUNT = Property.Int(Scope.NONE, "Amount");
    private final StringListProp CONDITIONS = Property.StringList(Scope.NONE, "Conditions");
    
    private final TerminableRegistry registry = TerminableRegistry.create();

    @Getter private final AchievementImpl achievement;
    
    @Getter private final int amount;
    private final Collection<SkillCondition> conditions = Lists.newArrayList();

    public Criteria(Achievement holder) {
        this.achievement = (AchievementImpl) holder;
        
        this.amount = AMOUNT.fget(achievement.getFile(), this);
        var conditionsIn = CONDITIONS.fget(achievement.getFile(), this);
    }

    public void incrementStat(Player player) {
        achievement.incrementIfSubscribed(player);
    }
        
    @Override
    public <T extends AutoCloseable> T bind(T terminable) {
        registry.accept((Terminable) terminable);
        return terminable;
    }
    
    @Override
    public String getPropertyNode() {
        return achievement.getPropertyNode() + ".Criteria";
    }

    @Override
    public void close() throws Exception {
        registry.terminate();
    }
}
