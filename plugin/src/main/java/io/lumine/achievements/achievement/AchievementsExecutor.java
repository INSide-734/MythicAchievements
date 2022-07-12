package io.lumine.achievements.achievement;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Maps;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.criteria.BlockBreakCriteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.NodeListProp;
import io.lumine.mythic.bukkit.utils.files.Files;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;

public class AchievementsExecutor extends ReloadableModule<MythicAchievementsPlugin> implements AchievementManager {

    private final NodeListProp CATEGORIES = Property.NodeList(Scope.CATEGORIES, "");
    private final NodeListProp NODES = Property.NodeList(Scope.NONE, "");
    
    private final Map<String,AchievementCategory> categories = Maps.newConcurrentMap();
    private final Map<String,Achievement> achievements = Maps.newConcurrentMap();
    
    public AchievementsExecutor(MythicAchievementsPlugin plugin) {
        super(plugin, false);
        
        load(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {
    
        for(var node : CATEGORIES.get()) {
            var cat = new AchievementCategoryImpl(this, node);

            categories.put(cat.getKey(), cat);
            Log.info("Loaded category {0}", node);
        }

        final var achievementsDir = new File(plugin.getDataFolder(), "achievements");
        for(var file : Files.getAllYaml(achievementsDir.getAbsolutePath())) {
            for(var node : NODES.fget(file)) {
                var achievement = new AchievementImpl(this, file, node);
                Log.info("Loading achievement {0}", node);
                achievements.put(achievement.getKey(), achievement);
            }
        }
        
        achievements.values().removeIf(achieve -> {
            var ach = (AchievementImpl) achieve;
            
            if(!ach.initialize()) {
                Log.error("Achievement {0} failed to initialize", ach.getKey());
                return true;
            }
            return false;
        });
        
        reinitializeOnlinePlayers();
    }

    @Override
    public void unload() {
        
    }

    @Override
    public Optional<Achievement> getAchievement(String name) {
        return Optional.ofNullable(achievements.getOrDefault(name, null));
    }

    @Override
    public Optional<AchievementCategory> getCategory(String key) {
        return Optional.ofNullable(categories.getOrDefault(key, null));
    }

    @Override
    public Optional<AchievementCriteria> getCriteria(Achievement achievement, String type) {
        return Optional.ofNullable(
                switch(type) {
                    case "BREAK_BLOCK" -> new BlockBreakCriteria(achievement);
                    default -> null;
                });
    }

    public void reinitializeOnlinePlayers() {
        
    }

    public Collection<AchievementCategory> getCategories() {
        return Collections.unmodifiableCollection(categories.values());
    }
    
    public Collection<Achievement> getAchievements() {
        return Collections.unmodifiableCollection(achievements.values());
    }
}
    
