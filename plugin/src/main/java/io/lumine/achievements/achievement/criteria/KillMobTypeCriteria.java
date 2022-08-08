package io.lumine.achievements.achievement.criteria;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.menu.Icon;

public class KillMobTypeCriteria extends Criteria {

    private final StringProp ENTITY_TYPE = Property.String(Scope.NONE, "EntityType", "ZOMBIE");
    
    private final String entityType;
    private final EntityType type;
    
    public KillMobTypeCriteria(String criteriaNode, Achievement holder) {
        super(criteriaNode, holder);
        
        this.entityType = ENTITY_TYPE.fget(holder.getFile(), this);
        
        this.type = EntityType.valueOf(entityType);
    }
    
    public void load() {
        Events.subscribe(EntityDeathEvent.class, EventPriority.MONITOR)
            .filter(event -> event.getEntity().getKiller() != null)
            .filter(event -> event.getEntityType() == type)
            .handler(event -> {
                final var player = event.getEntity().getKiller();
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
