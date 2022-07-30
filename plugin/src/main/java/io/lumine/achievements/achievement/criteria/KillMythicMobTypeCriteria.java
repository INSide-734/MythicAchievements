package io.lumine.achievements.achievement.criteria;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.menu.Icon;

public class KillMythicMobTypeCriteria extends Criteria {

    private final StringProp ENTITY_TYPE = Property.String(Scope.NONE, "MobType", "ZOMBIE");
    
    private final String entityType;
    
    public KillMythicMobTypeCriteria(Achievement holder) {
        super(holder);
        
        this.entityType = ENTITY_TYPE.fget(holder.getFile(), this);

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
