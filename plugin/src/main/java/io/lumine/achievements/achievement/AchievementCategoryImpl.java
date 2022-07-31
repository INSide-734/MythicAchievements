package io.lumine.achievements.achievement;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

import com.google.common.collect.Lists;

import io.lumine.achievements.achievement.serialization.AdvancementCategoryWrapper;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementFrame;
import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.EnumProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.LangProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.menu.Icon;
import io.lumine.mythic.bukkit.utils.menu.IconBuilder;
import lombok.Getter;
import lombok.Setter;

public class AchievementCategoryImpl implements AchievementCategory {

    protected static final LangProp DISPLAY = Property.Lang(Scope.CATEGORIES, "Display");
    protected static final LangProp DESCRIPTION = Property.Lang(Scope.CATEGORIES, "Description");
    
    protected static final EnumProp<Material> MATERIAL = Property.Enum(Scope.CATEGORIES, Material.class, "Icon.Material", Material.NETHER_STAR);
    protected static final IntProp MODEL = Property.Int(Scope.CATEGORIES, "Icon.Model");
    protected static final StringProp BACKGROUND = Property.String(Scope.CATEGORIES, "Background", "minecraft:textures/block/blackstone.png");
    protected static final EnumProp<AchievementFrame> FRAME = Property.Enum(Scope.CATEGORIES, AchievementFrame.class, "Frame", AchievementFrame.GOAL);
    
    @Getter private final AchievementsExecutor manager;
    @Getter private final String key;
    @Getter private final NamespacedKey namespacedKey;
    
    @Getter protected String title;
    @Getter protected String description;
    
    @Getter protected String background;
    @Getter protected AchievementFrame frame;
    
    @Getter protected Material iconMaterial;
    @Getter protected int iconData;   
    @Getter protected boolean hidden = false;
    
    @Getter private AdvancementCategoryWrapper advancementWrapper;
    
    @Getter public List<Achievement> achievements = Lists.newArrayList();
    @Getter public List<Achievement> baseAchievements = Lists.newArrayList();
    
    public AchievementCategoryImpl(AchievementsExecutor manager, String key) {
        this.manager = manager;
        this.key = key;
        this.namespacedKey = new NamespacedKey(manager.getPlugin(), key);

        this.title = DISPLAY.get(this);
        this.description = DESCRIPTION.get(this);
        this.background = BACKGROUND.get(this);
        this.frame = FRAME.get(this);
        
        this.iconMaterial = MATERIAL.get(this);
        this.iconData = MODEL.get(this);
        
        this.advancementWrapper = new AdvancementCategoryWrapper(this);
    }
    
    @Override
    public String getPropertyNode() {
        return key;
    }
    
    public Advancement getAdvancement() {
        return Bukkit.getAdvancement(this.namespacedKey);
    }

    @Override
    public Icon<AchievementProfile> getIcon() {
        return IconBuilder.<AchievementProfile>create()
                .material(Material.NETHER_STAR)
                .name(key)
                .click((prof,player) -> {
                
                    manager.getPlugin().getMenuManager().getAchievementsMenu().openMenu(player, this);
                    
                }).build();
    }

    public void addAchievement(Achievement achieve) {
        this.achievements.add(achieve);
        if(achieve.getParent().isEmpty()) {
            this.baseAchievements.add(achieve);
        }
    }

}
