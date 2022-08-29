package io.lumine.achievements.achievement;

import java.io.File;
import java.io.FileWriter;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.serialization.MCMetaWrapper;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.mythic.bukkit.utils.files.Folders;
import io.lumine.mythic.bukkit.utils.gson.GsonProvider;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;

public class AdvancementGUIExecutor extends ReloadableModule<MythicAchievementsPlugin> {

    private AchievementsExecutor manager;
    
    public AdvancementGUIExecutor(AchievementsExecutor manager) {
        super(manager.getPlugin(), false);
        
        this.manager = manager;
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {
        registerAdvancements();
    }

    @Override
    public void unload() {
        
    }

    public void registerAdvancements() {
        this.clearAdvancements();
        this.disableVanillaAdvancements();

        for(var category : manager.getCategories()) {
            var categoryKey = category.getNamespacedKey();
            var categoryBase = GsonProvider.standard().toJson(((AchievementCategoryImpl) category).getAdvancementWrapper());
            
            if(Bukkit.getAdvancement(categoryKey) != null) {
                Log.error("Achievement Category {0} is already registered (duplicate key?)", category.getNamespacedKey().toString());
                continue;
            }

            Bukkit.getUnsafe().loadAdvancement(categoryKey, categoryBase);

            for(var achieve : category.getBaseAchievements()) {
                registerAdvancement(achieve);
            }
        }
        
        Bukkit.reloadData();
    }
    
    private void registerAdvancement(Achievement achieve) {
        var achieveKey = achieve.getNamespacedKey();
        var achieveJson = GsonProvider.standard().toJson(((AchievementImpl) achieve).getAdvancementWrapper());
        
        if(Bukkit.getAdvancement(achieveKey) != null) {
            Log.error("Achievement {0} is already registered (duplicate key?)", achieve.getKey());
            return;
        }
        Bukkit.getUnsafe().loadAdvancement(achieveKey, achieveJson);

        for(var child : achieve.getChildren()) {
            registerAdvancement(child);
        }
    }

    public void clearAdvancements() {
        var advancements = Bukkit.advancementIterator();
        var namespace = new NamespacedKey(getPlugin(), "CarsonJF");

        while(advancements.hasNext()) {
            var achieveKey = advancements.next().getKey();
            
            if(namespace.getNamespace().equals(achieveKey.getNamespace())) {
                Bukkit.getUnsafe().removeAdvancement(achieveKey);
            }
            
            if(achieveKey.getNamespace().equalsIgnoreCase(NamespacedKey.MINECRAFT)) {
                Log.info("removing {0}", achieveKey.toString());
                Bukkit.getUnsafe().removeAdvancement(achieveKey);
            }
        }
        Bukkit.reloadData();
    }
    
    public void disableVanillaAdvancements() {
        var advancements = Bukkit.advancementIterator();
        var namespace = NamespacedKey.MINECRAFT;
        
        var world = Bukkit.getWorlds().get(0);
        var worldFolder = world.getWorldFolder();
        var packFolder = new File(worldFolder, "datapacks/mythicachievements");
        var dataFolder = new File(packFolder, "data/minecraft/advancements");
        var mcmetaFile = new File(packFolder, "pack.mcmeta");
        
        try {
            Folders.deleteDirectory(packFolder);
        } catch(Exception | Error ex) {
            ex.printStackTrace();
        }
        
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        
        if(!mcmetaFile.exists()) {
            final var out = new MCMetaWrapper();
            try(var writer = new FileWriter(mcmetaFile)) {
                GsonProvider.get().toJson(out, writer);
                writer.flush();
            } catch(Exception | Error ex) {
                ex.printStackTrace();
            }
        }
        
        for(var tab : getPlugin().getConfiguration().getDisabledVanillaCategories()) {
            var tabFolder = new File(dataFolder, tab.getFolder());
            
            if(!tabFolder.exists()) {
                tabFolder.mkdir();
            }
            
            var storyFile = new File(tabFolder, "root.json");
            
            try {
                storyFile.createNewFile();
            } catch(Exception | Error ex) {
                ex.printStackTrace();
            }
        }
    }

}
    
