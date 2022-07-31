package io.lumine.achievements.achievement;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
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
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.core.skills.SkillCondition;
import lombok.Getter;

public abstract class Criteria implements AchievementCriteria,Terminable,TerminableConsumer {

    private final IntProp AMOUNT = Property.Int(Scope.NONE, "Amount", 1);
    private final StringListProp CONDITIONS = Property.StringList(Scope.NONE, "Conditions");
    
    private final TerminableRegistry registry = TerminableRegistry.create();

    @Getter private final AchievementImpl achievement;
    
    @Getter private final int amount;
    private Collection<SkillCondition> conditions = null;

    public Criteria(Achievement holder) {
        this.achievement = (AchievementImpl) holder;
        
        this.amount = AMOUNT.fget(achievement.getFile(), this);
        var conditionsIn = CONDITIONS.fget(achievement.getFile(), this);
        
        this.conditions = MythicBukkit.inst().getSkillManager().getConditions(conditionsIn);
    }

    public void incrementStat(Player player) {
        achievement.incrementIfSubscribed(player);
    }
    
    public boolean hasConditions() {
        return conditions != null && !conditions.isEmpty();
    }
     
    public boolean checkConditions(Player player) {
        if(!hasConditions()) {
            return true;
        }
        
        var aPlayer = BukkitAdapter.adapt(player);
        
        for(var condition : conditions) {
            if(!condition.evaluateEntity(aPlayer)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkConditions(Player player, Location location) {
        if(!hasConditions()) {
            return true;
        }
        
        var aPlayer = BukkitAdapter.adapt(player);
        var aLocation = BukkitAdapter.adapt(location);
        
        for(var condition : conditions) {
            if(!condition.evaluateToLocation(aPlayer, aLocation)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean checkConditions(Player player, Entity entity) {
        if(!hasConditions()) {
            return true;
        }
        var aPlayer = BukkitAdapter.adapt(player);
        var aEntity = BukkitAdapter.adapt(entity);
        
        for(var condition : conditions) {
            if(!condition.evaluateToEntity(aPlayer, aEntity)) {
                return false;
            }
        }
        return true;
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
