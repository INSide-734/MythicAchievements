package io.lumine.achievements.config;

import com.google.common.collect.Sets;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.VanillaAchievements;
import io.lumine.achievements.storage.StorageDriver;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.config.properties.types.EnumProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.SqlCredentialsProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringListProp;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import io.lumine.mythic.bukkit.utils.storage.sql.SqlCredentials;
import lombok.Getter;

import java.util.Collection;

public class Configuration extends ReloadableModule<MythicAchievementsPlugin> implements PropertyHolder {
    
    private static final IntProp CLOCK_INTERVAL = Property.Int(Scope.CONFIG, "Clock.Interval", 1);
    private static final EnumProp<StorageDriver> STORAGE_DRIVER = Property.Enum(Scope.CONFIG, StorageDriver.class, "Storage.Driver", StorageDriver.JSON); 
    private static final SqlCredentialsProp SQL_CREDENTIALS = Property.SqlCredentials(Scope.CONFIG, "Storage");
    
    private static final StringListProp DISABLED_VANILLA_ADV = Property.StringList(Scope.CONFIG, "DisabledVanillaCategories");
    
    @Getter private Collection<VanillaAchievements> disabledVanillaCategories = Sets.newHashSet();
    @Getter private boolean allowingMetrics = true;
    
    public Configuration(MythicAchievementsPlugin plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MythicAchievementsPlugin plugin) {
        Log.info("Loading Configuration...");

        for(var in : DISABLED_VANILLA_ADV.get(this)) {
            try {
                var tab = VanillaAchievements.valueOf(in);
                disabledVanillaCategories.add(tab);
            } catch(Exception | Error ex) {
                continue;
            }
        }
    }
  
    @Override
    public void unload() {}

    @Override
    public String getPropertyNode() {
        return "Configuration";
    }

    public int getClockInterval() {
        return CLOCK_INTERVAL.get(this);
    }
    
    public StorageDriver getStorageType() {
        return STORAGE_DRIVER.get(this);
    }
    
    public SqlCredentials getSqlCredentials() {
        return SQL_CREDENTIALS.get(this);
    }
    
}
