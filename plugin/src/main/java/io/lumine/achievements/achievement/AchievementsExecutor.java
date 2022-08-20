package io.lumine.achievements.achievement;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.reflect.ClassPath;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.criteria.*;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.utils.annotations.MythicAchievementCriteria;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.NodeListProp;
import io.lumine.mythic.bukkit.utils.files.Files;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.core.logging.MythicLogger;
import lombok.Getter;

public class AchievementsExecutor extends ReloadableModule<MythicAchievementsPlugin> implements AchievementManager {

    private final NodeListProp CATEGORIES = Property.NodeList(Scope.CATEGORIES, "");
    private final NodeListProp NODES = Property.NodeList(Scope.NONE, "");
    
    private final Map<String,AchievementCategory> categories = Maps.newConcurrentMap();
    private final Map<String,Achievement> achievements = Maps.newConcurrentMap();
    
    @Getter private final AdvancementGUIExecutor advancementGUIManager;
    
    public AchievementsExecutor(MythicAchievementsPlugin plugin) {
        super(plugin, false);
        
        this.advancementGUIManager = new AdvancementGUIExecutor(this);
        
        load(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {
    
        var categoriesFile = new File(plugin.getDataFolder(), "categories.yml");
        if(!categoriesFile.exists()) {
            plugin.saveResource("categories.yml", false);
        }
        
        int categoriesAdded = 0;
        for(var node : CATEGORIES.get()) {
            var cat = new AchievementCategoryImpl(this, node);

            categories.put(cat.getKey(), cat);
            categoriesAdded++;
            
            if(!plugin.isPremium() && categoriesAdded >= MFCAT) {
                break;
            }
        }

        final var achievementsDir = new File(plugin.getDataFolder(), "Achievements");
        if(!achievementsDir.exists()) {
            achievementsDir.mkdir();
            plugin.saveResource("Achievements/ExampleAchievements.yml", false);
            plugin.saveResource("Achievements/MonsterHunting.yml", false);
        }
        
        int achievementsAdded = 0;
        for(var file : Files.getAllYaml(achievementsDir.getAbsolutePath())) {
            for(var node : NODES.fget(file)) {
                var achievement = new AchievementImpl(this, file, node);
                //Log.info("Loading achievement {0}", node);
                achievements.put(achievement.getKey(), achievement);
                
                achievementsAdded++;
                if(!plugin.isPremium() && achievementsAdded >= MFACH) {
                    break;
                }
            }

            if(!plugin.isPremium() && achievementsAdded >= MFACH) {
                break;
            }
        }
        
        for(var pack : MythicBukkit.inst().getPackManager().getPacks()) {
            var folder = pack.getPackFolder("Achievements");
            
            if(folder.exists()) {
                for(var file : Files.getAllYaml(folder.getAbsolutePath())) {
                    for(var node : NODES.fget(file)) {
                        var achievement = new AchievementImpl(this, file, node);
                        //Log.info("Loading achievement {0} from pack {1}", node, pack.getName());
                        achievements.put(achievement.getKey(), achievement);
                        
                        achievementsAdded++;
                        if(!plugin.isPremium() && achievementsAdded >= MFACH) {
                            break;
                        }
                    }
                    if(!plugin.isPremium() && achievementsAdded >= MFACH) {
                        break;
                    }
                }
            }

            if(!plugin.isPremium() && achievementsAdded >= MFACH) {
                break;
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
        
        this.advancementGUIManager.load(plugin);
        
        reinitializeOnlinePlayers();
    }

    @Override
    public void unload() {
        
    }

    @Override
    public Collection<String> getAchievementNames() {
        return achievements.keySet();
    }
    
    @Override
    public Optional<Achievement> getAchievement(String name) {
        return Optional.ofNullable(achievements.getOrDefault(name, null));
    }

    @Override
    public Optional<AchievementCategory> getCategory(String key) {
        return Optional.ofNullable(categories.getOrDefault(key, null));
    }

    public void reinitializeOnlinePlayers() {
        
    }

    public Collection<AchievementCategory> getCategories() {
        return Collections.unmodifiableCollection(categories.values());
    }
    
    public Collection<Achievement> getAchievements() {
        return Collections.unmodifiableCollection(achievements.values());
    }
    

    /*================================================================================
     * 
     * CRITERIA
     * 
     *===============================================================================*/
    
    private static final Map<String, Class<? extends Criteria>> CRITERIA = new ConcurrentHashMap<>(); 
    
    public ImmutableMap<String,Class<? extends Criteria>> getCriteria() {
        return ImmutableMap.copyOf(CRITERIA);
    }

    @Override
    public Optional<AchievementCriteria> getCriteria(Achievement achievement, String criteriaNode, String type) {
        type = type.replace("_", "");
        
        if(CRITERIA.containsKey(type.toUpperCase())) {
            final Class<? extends Criteria> clazz = CRITERIA.get(type.toUpperCase());
            
            try {
                return Optional.of(clazz.getConstructor(String.class, Achievement.class).newInstance(criteriaNode,achievement));
            } catch (Exception e) {
                MythicLogger.error("Failed to construct AchievementCriteria {0}", type);
                e.printStackTrace();
            }
        } else {
            return Optional.of(new ManualCriteria(criteriaNode,achievement));
        }
        return Optional.empty();
    }
        
    /*================================================================================
     * 
     * ANNOTATION SHIT
     * 
     *===============================================================================*/
    
    static {
        try {
            Set<Class<?>> criteriaClasses = Sets.newConcurrentHashSet();
            for(var i : ClassPath.from(MythicAchievementsPlugin.inst().getClass().getClassLoader()).getAllClasses()) {
                try {
                    if(i.getPackageName().equalsIgnoreCase("io.lumine.achievements.achievement.criteria")) {
                        criteriaClasses.add(i.load());
                    }
                } catch(Exception | Error ex) {
                    ex.printStackTrace();
                }
            }

            for(Class<?> clazz : criteriaClasses) {
                try {
                    final var annotation = clazz.getAnnotation(MythicAchievementCriteria.class);
                    final String name = annotation.name();
                    final String[] aliases = annotation.aliases();
                    
                    if(Criteria.class.isAssignableFrom(clazz)) {
                        CRITERIA.put(name.toUpperCase(), (Class<? extends Criteria>) clazz);
                        for(String alias : aliases) {
                            CRITERIA.put(alias.toUpperCase(), (Class<? extends Criteria>) clazz);
                        }
                    }
                } catch(Exception ex) {
                    MythicLogger.error("Failed to load AchievementCriteria class {0}", clazz.getCanonicalName());
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private static final int MFCAT = 2;
    private static final int MFACH = 10;
}
    
