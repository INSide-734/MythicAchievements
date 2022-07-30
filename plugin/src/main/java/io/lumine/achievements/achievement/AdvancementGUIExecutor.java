package io.lumine.achievements.achievement;

import org.bukkit.Bukkit;
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
    
    }

    @Override
    public void unload() {
        
    }

    public void registerAdvancements() {
        this.clearAdvancements();
        
        Bukkit.reloadData();
        
        for(var category : getPlugin().getAchievementManager().getCategories()) {
            var categoryKey = category.getNamespacedKey();
            var categoryBase = GsonProvider.standard().toJson(((AchievementCategoryImpl) category).getAdvancementWrapper());
            
            if(Bukkit.getAdvancement(categoryKey) != null) {
                Log.error("Achievement Category {0} is already registered (duplicate key?)", category.getNamespacedKey().toString());
                continue;
            }

            Log.info("Loading category json {0}", categoryBase);
            final var cadv = Bukkit.getUnsafe().loadAdvancement(categoryKey, categoryBase);
            category.setAdvancement(cadv);
            
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
        final var adv = Bukkit.getUnsafe().loadAdvancement(achieveKey, achieveJson);
        achieve.setAdvancement(adv);
        
        for(var child : achieve.getChildren()) {
            Log.info("---- Loading achievement {0}", child.getKey());
            registerAdvancement(child);
        }
    }

    private void clearAdvancements() {
        var advancements = Bukkit.advancementIterator();
        
        while(advancements.hasNext()) {
            var achieveKey = advancements.next().getKey();
            
            if(Constants.CRITERIA_KEY.equals(achieveKey.getNamespace())) {
                Bukkit.getUnsafe().removeAdvancement(achieveKey);
            }
        }
    }

}
    
