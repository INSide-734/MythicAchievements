package io.lumine.achievements.achievement;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.lumine.achievements.achievement.serialization.AdvancementWrapper;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.api.achievements.AchievementFrame;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.mobs.GenericCaster;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.Players;
import io.lumine.mythic.bukkit.utils.adventure.text.Component;
import io.lumine.mythic.bukkit.utils.adventure.text.TextReplacementConfig;
import io.lumine.mythic.bukkit.utils.adventure.text.event.HoverEvent;
import io.lumine.mythic.bukkit.utils.adventure.text.format.NamedTextColor;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.EnumProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.LangProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.NodeListProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringListProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.items.ItemFactory;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.menu.Icon;
import io.lumine.mythic.bukkit.utils.menu.IconBuilder;
import io.lumine.mythic.bukkit.utils.menu.MenuData;
import io.lumine.mythic.bukkit.utils.text.Text;
import io.lumine.mythic.core.config.MythicLineConfigImpl;
import io.lumine.mythic.core.drops.DropMetadataImpl;
import io.lumine.mythic.core.drops.DropTable;
import io.lumine.mythic.core.logging.MythicLogger;
import io.lumine.mythic.core.logging.MythicLogger.DebugLevel;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.SkillTriggers;
import io.lumine.mythic.core.skills.TriggeredSkill;
import lombok.Getter;

public class AchievementImpl extends Achievement implements MenuData<AchievementProfile> {

    protected static final StringProp NAMESPACE = Property.String(Scope.NONE, "Namespace", null);
    
    protected static final LangProp DISPLAY = Property.Lang(Scope.NONE, "Display");
    protected static final LangProp DESCRIPTION = Property.Lang(Scope.NONE, "Description", "");
    protected static final StringProp PARENT = Property.String(Scope.NONE, "Parent", null);
    protected static final StringProp CATEGORY = Property.String(Scope.NONE, "Category", null);
    protected static final NodeListProp CRITERIA = Property.NodeList(Scope.NONE, "Criteria");
    protected static final EnumProp<AchievementFrame> FRAME = Property.Enum(Scope.CATEGORIES, AchievementFrame.class, "Frame", AchievementFrame.GOAL);
    
    protected static final EnumProp<Material> MATERIAL = Property.Enum(Scope.NONE, Material.class, "Icon.Material", Material.EMERALD);
    protected static final IntProp MODEL = Property.Int(Scope.NONE, "Icon.Model");
    protected static final StringProp TEXTURE = Property.String(Scope.NONE, "Icon.SkullTexture");

    protected static final StringProp REWARD_MESSAGE = Property.String(Scope.NONE, "Reward.Message", null);
    protected static final StringListProp REWARD_DROPS = Property.StringList(Scope.NONE, "Reward.Drops");
    protected static final StringListProp REWARD_SKILLS = Property.StringList(Scope.NONE, "Reward.Skills");
    
    @Getter private final File file;
    @Getter private final NamespacedKey namespacedKey;
    
    @Getter private String title;
    @Getter private String description;
    @Getter private AchievementFrame frame;
    @Getter private boolean hidden = false;
    
    @Getter private String categoryName;
    @Getter private String parentName;
    
    @Getter private AchievementCategory category;
    @Getter private Optional<Achievement> parent = Optional.empty();
    @Getter private Collection<Achievement> children = Lists.newArrayList();
    private Map<String,AchievementCriteria> criteria = Maps.newConcurrentMap();
    
    private ItemStack icon;
    @Getter private Material iconMaterial;
    @Getter private int iconModel;
    @Getter private String iconNBT;
    
    private final Component rewardMessage;
    private final DropTable rewardDrops;
    private final List<SkillMechanic> rewardSkills = Lists.newArrayList();

    @Getter private ItemStack menuItem;
    
    @Getter private AdvancementWrapper advancementWrapper;
    
    private final Map<UUID,AchievementProfile> subscribedPlayers = Maps.newConcurrentMap();

    public AchievementImpl(AchievementsExecutor manager, File file, String key) {
        super(manager, key);
        
        this.file = file;

        this.title = DISPLAY.fget(file,this);
        this.description = DESCRIPTION.fget(file,this);
        this.frame = FRAME.fget(file,this);
        
        this.categoryName = CATEGORY.fget(file,this);
        this.parentName = PARENT.fget(file,this);
        
        var nkey = (categoryName != null) ? categoryName + "/" + key : key;
        this.namespacedKey = new NamespacedKey(manager.getPlugin(), nkey);
        
        this.iconMaterial = MATERIAL.fget(file,this);
        
        this.iconModel = MODEL.fget(file,this);
        
        var icon = ItemFactory.of(iconMaterial).name(title).model(iconModel).hideAttributes().build();
        this.iconNBT = MythicBukkit.inst().getVolatileCodeHandler().getItemHandler().dumpNBTData(icon);

        final var rMessage = REWARD_MESSAGE.fget(file,this);
        if(rMessage == null) {
            this.rewardMessage = null;
        } else {
            this.rewardMessage = Text.parse(rMessage);
        }
        
        final List<String> drops = REWARD_DROPS.fget(file,this);
        if(drops.isEmpty()) {
            this.rewardDrops = null;
        } else {
            this.rewardDrops = new DropTable(file.getAbsolutePath(), "Achievement:"+getKey(), drops, true);
        }
        
        final List<String> skills = REWARD_SKILLS.fget(file,this);
        for(var line : skills)   {
            line = MythicLineConfigImpl.unparseBlock(line);
            var mechanic = MythicBukkit.inst().getSkillManager().getMechanic(line);

            if(mechanic != null)    {
                rewardSkills.add(mechanic);
            }
        }
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
            return false;
        }
        this.category = maybeCategory.get();
        
        for(var criteriaNode : CRITERIA.fget(file,this)) {
            var criteriaType = Property.String(Scope.NONE, "Criteria."+criteriaNode+".Type", null).fget(file,this);
            
            if(criteriaType == null) {
                Log.error("No criteria type {0} for achievement {1}", criteriaNode, getKey());
                return false;
            }
            
            var maybeCriteria = getManager().getCriteria(this, criteriaNode, criteriaType);
            if(maybeCriteria.isEmpty()) {
                Log.error("Invalid criteria type {0} for achievement {1}", criteriaNode, getKey());
                return false;
            }
            this.criteria.put(criteriaNode, maybeCriteria.get());
        }
        if(criteria.size() == 0) {
            Log.error("No criteria loaded for achievement {1}", getKey());
            return false;
        }

        if(this.parent.isPresent()) {
            this.parent.get().getChildren().add(this);
        }
        this.category.addAchievement(this);
        
        this.advancementWrapper = new AdvancementWrapper(this);
        
        if(iconMaterial == Material.PLAYER_HEAD) {
            this.menuItem = ItemFactory.of(this.iconMaterial)
                    .name(Text.colorize(this.getTitle()))
                    .model(iconModel)
                    .hideAttributes()
                    .lore(description)
                    .skullTexture(TEXTURE.get(this))
                    .build();
        } else {
            this.menuItem = ItemFactory.of(this.iconMaterial)
                    .name(Text.colorize(this.getTitle()))
                    .model(iconModel)
                    .hideAttributes()
                    .lore(description)
                    .build();
        }
        return true;
    }
    
    public Collection<AchievementCriteria> getCriteria() {
        return criteria.values();
    }
    
    public void subscribe(AchievementProfile profile) {
        subscribedPlayers.put(profile.getPlayer().getUniqueId(), profile);
        for(var criteria : this.criteria.values()) {
            criteria.loadListeners();
        }
    }
    
    public void unsubscribe(AchievementProfile profile) {
        subscribedPlayers.remove(profile.getPlayer().getUniqueId());
        
        if(subscribedPlayers.isEmpty()) {
            for(var criteria : this.criteria.values()) {
                criteria.unloadListeners();
            }
        }
    }
    
    @Override
    public Collection<AchievementProfile> getSubscribedPlayers() {
        return subscribedPlayers.values();
    }

    public void incrementIfSubscribed(Player player, AchievementCriteria criteria, int amount) {
        var sub = (ProfileImpl) subscribedPlayers.get(player.getUniqueId());

        if(sub != null) {
            sub.incrementAchievementStat(this, criteria, amount);
        }
    }
    
    public Advancement getAdvancement() {
        return Bukkit.getAdvancement(this.namespacedKey);
    }

    @Override
    public void sendCompletedMessage(Player player) {
        player.sendTitle("", Text.colorizeLegacy("<green>Achievement Completed"), 0, 60, 20);
        
        var out = Text.parse("<white><name> has made the advancement <green><advancement>");

        var nameReplacement = TextReplacementConfig.builder()
                .matchLiteral("<name>")
                .replacement(matchResult -> Component.text(player.getName())).build();
        
        var advReplacement = TextReplacementConfig.builder()
                .matchLiteral("<advancement>")
                .replacement(matchResult -> Component.text()
                            .colorIfAbsent(NamedTextColor.GREEN)
                            .append(Component.text("["))
                            .append(Component.text(title))
                            .append(Component.text("]"))
                            .hoverEvent(
                                    HoverEvent.showText(
                                            Component.text()
                                                    .colorIfAbsent(NamedTextColor.GREEN)
                                                    .append(Component.text(title))
                                                    .append(Component.text("\n"))
                                                    .append(Component.text(description))))
                ).build();
        
        out = out.replaceText(nameReplacement);
        out = out.replaceText(advReplacement);
        
        for(var p : Players.all()) {
            Text.sendMessage(p, out);
        }
    }
    
    @Override
    public void giveRewards(Player player) {
        if(this.rewardMessage != null) {
            Text.sendMessage(player, rewardMessage);
        }
        if(this.rewardSkills != null) {
            var aPlayer = BukkitAdapter.adapt(player);
            
            var ts = new TriggeredSkill(SkillTriggers.API, new GenericCaster(aPlayer), aPlayer.getLocation(), aPlayer, rewardSkills, true,
                    (meta) -> {
                        //meta.getVariables().putString("equip-slot", equippedSlot);
                        //meta.getVariables().putObject("equip-item", itemStack);
                    });
        }
        if(this.rewardDrops != null) {
            var aPlayer = BukkitAdapter.adapt(player);
            var meta = new DropMetadataImpl(new GenericCaster(aPlayer), aPlayer);
            
            rewardDrops.generate(meta).give(aPlayer);
        }
    }
    
    @Override
    public Icon<AchievementProfile> getIcon() {
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
        
}
