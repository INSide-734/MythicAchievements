package io.lumine.achievements.achievement.criteria;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.google.common.collect.Sets;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.utils.annotations.MythicAchievementCriteria;
import io.lumine.mythic.bukkit.utils.Events;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.menu.Icon;

@MythicAchievementCriteria(name="blockPlace", aliases={"placeBlock","placeBlockType","placeBlocks"})
public class BlockPlaceTypeCriteria extends Criteria {

    private final StringProp BLOCK_TYPE = Property.String(Scope.NONE, "Block");
    
    private final String blockType;
    private final Set<Material> blocks = Sets.newConcurrentHashSet();
    
    public BlockPlaceTypeCriteria(String criteriaNode, Achievement holder) {
        super(criteriaNode, holder);
        
        this.blockType = BLOCK_TYPE.fget(holder.getFile(), this);
        
        for(var spl : blockType.split(",")) {
            try {
                this.blocks.add(Material.valueOf(spl));
            } catch(Exception | Error ex) {
                Log.error("Invalid material type {0} for BlockPlaceCriteria", spl);
            }
        }
    }
    
    public void load() {
        Events.subscribe(BlockPlaceEvent.class)
            .filter(event -> blocks.contains(event.getBlock().getType()))
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
