package io.lumine.achievements.achievement;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.core.drops.DropTable;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.BooleanProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.EnumProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.LangListProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.LangProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.NodeListProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.items.ItemFactory;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.menu.Icon;
import io.lumine.mythic.bukkit.utils.menu.IconBuilder;
import io.lumine.mythic.bukkit.utils.text.Text;
import lombok.Getter;

public class AchievementImpl extends Achievement {

    protected static final StringProp NAMESPACE = Property.String(Scope.NONE, "Namespace", null);
    
    protected static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
    protected static final LangProp GOAL = Property.Lang(Scope.NONE, "Goal");
    protected static final StringProp PARENT = Property.String(Scope.NONE, "Parent", null);
    protected static final StringProp CATEGORY = Property.String(Scope.NONE, "Category", null);
    protected static final StringProp CRITERIA = Property.String(Scope.NONE, "Criteria.Type", null);
    
    protected static final EnumProp<Material> MATERIAL = Property.Enum(Scope.NONE, Material.class, "Icon.Material", Material.EMERALD);
    protected static final IntProp MODEL = Property.Int(Scope.NONE, "Icon.Model");
    protected static final StringProp TEXTURE = Property.String(Scope.NONE, "Icon.SkullTexture");

       
    /*
    private final String iconItem;
    private final String iconData;
    private final String title;
    private final String description;
    private final String parent;
    private final String background;
    private final String frame;
    */
    
    @Getter protected final File file;

    @Getter protected String display;
    @Getter protected String goal;
    @Getter protected String categoryName;
    @Getter protected String parentName;
    @Getter protected String criteriaType;
    
    @Getter private AchievementCategory category;
    @Getter private Optional<Achievement> parent = Optional.empty();
    @Getter private AchievementCriteria criteria;
    
    protected Material iconMaterial;
    protected int iconModel;
    
    //private final DropTable reward;

    @Getter protected ItemStack menuItem;
    
    @Getter private final Map<UUID,Profile> subscribedPlayers = Maps.newConcurrentMap();

    public AchievementImpl(AchievementManager manager, File file, String key) {
        super(manager, key);
        
        this.file = file;
        
        this.display = DISPLAY.fget(file,this);
        this.goal = GOAL.fget(file,this);
        
        this.categoryName = CATEGORY.fget(file,this);
        this.parentName = PARENT.fget(file,this);
        this.criteriaType = CRITERIA.fget(file,this);
        
        this.iconMaterial = MATERIAL.fget(file,this);
        this.iconModel = MODEL.fget(file,this);
    }
    
    public boolean initialize() {
        var maybeCategory = getManager().getCategory(categoryName);
        
        if(maybeCategory.isEmpty()) {
            Log.info("-- Failed to find category {0}", categoryName);
            return false;
        }
        this.category = maybeCategory.get();
        this.category.addAchievement(this);
        
        if(this.parentName != null) {
            var maybeParent = getManager().getAchievement(parentName);
            
            if(maybeParent.isEmpty()) {
                return false;
            }
            this.parent = maybeParent;
        }
        
        var maybeCriteria = getManager().getCriteria(this,criteriaType);
        if(maybeCriteria.isEmpty()) {
            return false;
        }
        this.criteria = maybeCriteria.get();
        
        if(iconMaterial == Material.PLAYER_HEAD) {
            this.menuItem = ItemFactory.of(this.iconMaterial)
                    .name(Text.colorize(this.getDisplay()))
                    .model(iconModel)
                    .hideAttributes()
                    .lore(goal)
                    .skullTexture(TEXTURE.get(this))
                    .build();
        } else {
            this.menuItem = ItemFactory.of(this.iconMaterial)
                    .name(Text.colorize(this.getDisplay()))
                    .model(iconModel)
                    .hideAttributes()
                    .lore(goal)
                    .build();
        }
        
        return true;
    }

    public void incrementIfSubscribed(Player player, int amount) {
        var sub = subscribedPlayers.get(player.getUniqueId());
        
        if(sub != null) {
            sub.incrementAchievementStat(this, amount);
        }
    }

    @Override
    public void sendCompletedMessage(Player player) {
        Text.sendMessage(player, "achievement completed");
    }
    
    @Override
    public void giveRewards(Player player) {
        Text.sendMessage(player, "rewards given");
    }
    
    protected Icon<AchievementProfile> buildIcon(String type) {
        return IconBuilder.<AchievementProfile>create()
                .name(Text.colorize(this.getDisplay()))
                .itemStack(this.menuItem)
                .hideFlags()
                .lore(prof -> {
                    List<String> desc = Lists.newArrayList(goal);
                    if(!prof.has(this)) {
                        desc.add("");
                        desc.add(Text.colorizeLegacy("<red>Not Unlocked"));
                    }
                    return desc; 
                })
                .click((prof,player) -> {

                }).build();
    }
    
    public boolean isValid() {
        return category != null && criteria != null && (parentName == null || parent != null);
    }
    
    @Override
    public String getPropertyNode() {
        return getKey();
    }

    @Override
    public Icon<AchievementProfile> getIcon() {
        // TODO Auto-generated method stub
        return null;
    }


        
}
