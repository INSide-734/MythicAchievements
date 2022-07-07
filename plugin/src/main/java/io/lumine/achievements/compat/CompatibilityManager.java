package io.lumine.achievements.compat;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.utils.logging.Log;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Optional;

public class CompatibilityManager extends ReloadableModule<MythicAchievementsPlugin> {

    @Getter private Optional<LumineCoreCompat> lumineCore = Optional.empty();
    @Getter private Optional<MythicMobsCompat> mythicMobs = Optional.empty();
    @Getter private Optional<PlaceholderAPICompat> papi = Optional.empty();
    
    public CompatibilityManager(MythicAchievementsPlugin plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MythicAchievementsPlugin plugin) {
        if(Bukkit.getPluginManager().getPlugin("LumineCore") != null) {
            Log.info("Found LumineCore, enabling compatibility.");
            lumineCore = Optional.of(new LumineCoreCompat(plugin));
        }
        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            Log.info("MythicMobs found; enabling MythicMobs support.");
            mythicMobs = Optional.of(new MythicMobsCompat(plugin));
        }
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Log.info("PlaceholderAPI found; enabling PlaceholderAPI support.");
            papi = Optional.of(new PlaceholderAPICompat(plugin));
            papi.get().register();
        }

    }
  
    @Override
    public void unload() {
        
    }

}
