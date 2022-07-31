package io.lumine.achievements.achievement;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.constants.Constants;
import io.lumine.mythic.bukkit.utils.gson.GsonProvider;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;

public class AdvancementGUIExecutor extends ReloadableModule<MythicAchievementsPlugin> {

    public AdvancementGUIExecutor(AchievementsExecutor manager) {
        super(manager.getPlugin(), false);
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

        for(var category : getPlugin().getAchievementManager().getCategories()) {
            var categoryKey = category.getNamespacedKey();
            var categoryBase = GsonProvider.standard().toJson(((AchievementCategoryImpl) category).getAdvancementWrapper());
            
            if(Bukkit.getAdvancement(categoryKey) != null) {
                Log.error("Achievement Category {0} is already registered (duplicate key?)", category.getNamespacedKey().toString());
                continue;
            }

            Log.info("Loading category json {0}", categoryBase);
            Bukkit.getUnsafe().loadAdvancement(categoryKey, categoryBase);

            for(var achieve : category.getBaseAchievements()) {
                Log.info("-- Loading base achievement {0}", achieve.getKey());
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
        Log.info("Loading {0}", achieveJson);
        Bukkit.getUnsafe().loadAdvancement(achieveKey, achieveJson);

        for(var child : achieve.getChildren()) {
            Log.info("---- Loading achievement {0}", child.getKey());
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
        }
        Bukkit.reloadData();
    }

}
    
