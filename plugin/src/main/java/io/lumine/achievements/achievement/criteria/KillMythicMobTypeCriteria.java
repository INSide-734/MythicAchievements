package io.lumine.achievements.achievement.criteria;

import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.utils.annotations.MythicAchievementCriteria;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.menu.Icon;

@MythicAchievementCriteria(name="killMythicMobs", aliases={"killMythicMob","killMythicMobType","killMythicEntity","killMythicEntityType"})
public class KillMythicMobTypeCriteria extends Criteria {

    private final StringProp ENTITY_TYPE = Property.String(Scope.NONE, "MobType", "ZOMBIE");
    
    private final String entityType;
    
    public KillMythicMobTypeCriteria(String criteriaNode, Achievement holder) {
        super(criteriaNode, holder);
        
        this.entityType = ENTITY_TYPE.fget(holder.getFile(), this);
    }
    
    public void load() {
        Events.subscribe(MythicMobDeathEvent.class, EventPriority.MONITOR)
            .filter(event -> event.getKiller() != null && event.getKiller() instanceof Player)
            .filter(event -> event.getMobType().getInternalName().equals(entityType))
            .handler(event -> {
                final var player = (Player) event.getKiller();
                final var entity = event.getEntity();
                
                if(checkConditions(player,entity)) {
                    incrementStat(player);
                }
            })
            .bindWith(this);
    }

    @Override
    public Icon<AchievementProfile> getIcon() {
        return null;
    }

}
