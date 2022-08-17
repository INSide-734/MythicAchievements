package io.lumine.achievements.config;

import com.google.common.collect.Lists;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.storage.StorageDriver;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.PropertyHolder;
import io.lumine.mythic.bukkit.utils.config.properties.types.EnumProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Configuration extends ReloadableModule<MythicAchievementsPlugin> implements PropertyHolder {
    
    private static final IntProp CLOCK_INTERVAL = Property.Int(Scope.CONFIG, "Clock.Interval", 1);
    private static final EnumProp<StorageDriver> STORAGE_DRIVER = Property.Enum(Scope.CONFIG, StorageDriver.class, "Storage.Driver", StorageDriver.JSON); 
 
    @Getter private boolean allowingMetrics = true;
    
    public Configuration(MythicAchievementsPlugin plugin)  {
        super(plugin);
    }
    
    @Override
    public void load(MythicAchievementsPlugin plugin) {
        Log.info("Loading Configuration...");
        generateDefaultConfigFiles();
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
    
    private void generateDefaultConfigFiles() {
        final var menuFolder = new File(plugin.getDataFolder(), "menus");
        final var packFolder = new File(plugin.getDataFolder(), "packs");
        final var demoFolder = new File(packFolder, "starter");
        
        final String copyFolder;
        if(getPlugin().isPremium()) {
            copyFolder = "premium";
        } else {
            copyFolder = "default";
        }
        
        if(!menuFolder.exists()) {
            Log.info("Generating Menu files...");

            if(menuFolder.mkdir()) {
                try {
                    JarFile jarFile = new JarFile(getPlugin().getJarFile());
                    for(Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                        String name = entries.nextElement().getName();
                        if(name.startsWith(copyFolder + "/menus/") && name.length() > (copyFolder + "/menus/").length()) {
                            Files.copy(getPlugin().getResource(name), new File(getPlugin().getDataFolder() + "/menus", name.split("/")[2]).toPath());
                        }
                    }
                    jarFile.close();
                } catch(IOException ex) {
                    Log.error("Could not load default menu configuration.");
                    ex.printStackTrace();
                }
            } else Log.error("Could not create directory!");
        }
        
    }
    
}
