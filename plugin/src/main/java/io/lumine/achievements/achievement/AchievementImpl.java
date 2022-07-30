package io.lumine.achievements.achievement;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import io.lumine.achievements.achievement.serialization.AdvancementWrapper;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.api.achievements.AchievementFrame;
import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.players.Profile;
import io.lumine.core.util.FireworkUtil;
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
import lombok.Setter;

public class AchievementImpl extends Achievement {

    protected static final StringProp NAMESPACE = Property.String(Scope.NONE, "Namespace", null);
    
    protected static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
    protected static final LangProp DESCRIPTION = Property.Lang(Scope.NONE, "Description", "");
    protected static final StringProp PARENT = Property.String(Scope.NONE, "Parent", null);
    protected static final StringProp CATEGORY = Property.String(Scope.NONE, "Category", null);
    protected static final StringProp CRITERIA = Property.String(Scope.NONE, "Criteria.Type", null);
    protected static final EnumProp<AchievementFrame> FRAME = Property.Enum(Scope.CATEGORIES, AchievementFrame.class, "Frame", AchievementFrame.GOAL);
    
    protected static final EnumProp<Material> MATERIAL = Property.Enum(Scope.NONE, Material.class, "Icon.Material", Material.EMERALD);
    protected static final IntProp MODEL = Property.Int(Scope.NONE, "Icon.Model");
    protected static final StringProp TEXTURE = Property.String(Scope.NONE, "Icon.SkullTexture");

    @Getter private final File file;
    @Getter private final NamespacedKey namespacedKey;
    
    @Getter private String title;
    @Getter private String description;
    @Getter private AchievementFrame frame;
    @Getter private boolean hidden = false;
    
    @Getter private String categoryName;
    @Getter private String parentName;
    @Getter private String criteriaType;
    
    @Getter private AchievementCategory category;
    @Getter private Optional<Achievement> parent = Optional.empty();
    @Getter private Collection<Achievement> children = Lists.newArrayList();
    @Getter private AchievementCriteria criteria;
    
    @Getter private Material iconMaterial;
    @Getter private int iconData;
    
    //private final DropTable reward;

    @Getter private ItemStack menuItem;
    
    @Getter @Setter private transient Advancement advancement;
    @Getter private AdvancementWrapper advancementWrapper;
    
    @Getter private final Map<UUID,Profile> subscribedPlayers = Maps.newConcurrentMap();

    public AchievementImpl(AchievementsExecutor manager, File file, String key) {
        super(manager, key);
        
        this.file = file;
        this.namespacedKey = new NamespacedKey(manager.getPlugin(), key);

        this.title = DISPLAY.fget(file,this);
        this.description = DESCRIPTION.fget(file,this);
        this.frame = FRAME.fget(file,this);
        
        this.categoryName = CATEGORY.fget(file,this);
        this.parentName = PARENT.fget(file,this);
        this.criteriaType = CRITERIA.fget(file,this);
        
        this.iconMaterial = MATERIAL.fget(file,this);
        this.iconData = MODEL.fget(file,this);
    }
    
    public boolean initialize() {
        
        if(this.parentName != null) {
            var maybeParent = getManager().getAchievement(parentName);
            
            if(maybeParent.isEmpty()) {
                return false;
            }
            this.parent = maybeParent;
        }
        
        var maybeCategory = getManager().getCategory(categoryName);
        
        if(maybeCategory.isEmpty()) {
            Log.info("-- Failed to find category {0}", categoryName);
            return false;
        }
        this.category = maybeCategory.get();
        
        var maybeCriteria = getManager().getCriteria(this,criteriaType);
        if(maybeCriteria.isEmpty()) {
            return false;
        }
        this.criteria = maybeCriteria.get();
        
        if(this.parent.isPresent()) {
            this.parent.get().getChildren().add(this);
        }
        this.category.addAchievement(this);
        
        this.advancementWrapper = new AdvancementWrapper(this);
        
        if(iconMaterial == Material.PLAYER_HEAD) {
            this.menuItem = ItemFactory.of(this.iconMaterial)
                    .name(Text.colorize(this.getTitle()))
                    .model(iconData)
                    .hideAttributes()
                    .lore(description)
                    .skullTexture(TEXTURE.get(this))
                    .build();
        } else {
            this.menuItem = ItemFactory.of(this.iconMaterial)
                    .name(Text.colorize(this.getTitle()))
                    .model(iconData)
                    .hideAttributes()
                    .lore(description)
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
        player.sendTitle("", Text.colorize("<green>Achievement Completed"), 0, 20, 20);
        Text.sendMessage(player, "achievement completed");
    }
    
    @Override
    public void giveRewards(Player player) {
        Text.sendMessage(player, "rewards given");
    }
    
    protected Icon<AchievementProfile> buildIcon(String type) {
        return IconBuilder.<AchievementProfile>create()
                .name(Text.colorize(this.getTitle()))
                .itemStack(this.menuItem)
                .hideFlags()
                .blink((prof,player) -> {
                    if(prof.hasCompleted(this) && !prof.hasCollectedReward(this)) {
                        return ItemFactory.of(Material.AIR);
                    } else {
                        return null;
                    }
                })
                .lore(prof -> {
                    List<String> desc = Lists.newArrayList(description);
                    if(!prof.hasCompleted(this)) {
                        desc.add("");
                        desc.add(Text.colorizeLegacy("<red>Not Completed"));
                    } else {
                        desc.add("");
                        desc.add(Text.colorizeLegacy("<green><bold>Completed"));
                    }
                    return desc; 
                })
                .click((prof,player) -> {
                    if(prof.hasCompleted(this) && !prof.hasCollectedReward(this)) {
                        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1, 1);
                        prof.setRewardsCollected(this);
                        giveRewards(player);
                    }
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
