package io.lumine.achievements.achievement.criteria;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.menu.Icon;

public class BlockBreakTypeCriteria extends Criteria {

    private final StringProp BLOCK_TYPE = Property.String(Scope.NONE, "Block");
    
    private final String blockType;
    private final Material block;
    
    public BlockBreakTypeCriteria(Achievement holder) {
        super(holder);
        
        this.blockType = BLOCK_TYPE.fget(holder.getFile(), this);
        
        this.block = Material.valueOf(blockType);
        
        Events.subscribe(BlockBreakEvent.class)
            .filter(event -> event.getBlock().getType() == block)
            .handler(event -> {
                final var player = event.getPlayer();
                
                if(checkConditions(player)) {
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
