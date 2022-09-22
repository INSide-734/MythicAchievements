package io.lumine.achievements;

import io.lumine.achievements.achievement.AchievementsExecutor;
import io.lumine.achievements.commands.BaseCommand;
import io.lumine.achievements.commands.admin.AdminCommand;
import io.lumine.achievements.compat.CompatibilityManager;
import io.lumine.achievements.compat.WorldGuardSupport;
import io.lumine.achievements.config.Configuration;
import io.lumine.achievements.listeners.PlayerListeners;
import io.lumine.achievements.logging.MCLogger;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.metrics.bStats;
import io.lumine.achievements.nms.VolatileCodeDisabled;
import io.lumine.achievements.nms.VolatileCodeHandler;
import io.lumine.achievements.players.ProfileManager;
import io.lumine.mythic.bukkit.utils.chat.ColorString;
import io.lumine.mythic.bukkit.utils.logging.ConsoleColor;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.plugin.LuminePlugin;
import io.lumine.mythic.bukkit.utils.version.ServerVersion;
import lombok.Getter;

import java.io.File;

import org.bukkit.Bukkit;

public class MythicAchievementsPlugin extends LuminePlugin {

    private static MythicAchievementsPlugin plugin;

    @Getter private Configuration configuration;
    @Getter private CompatibilityManager compatibility;
    @Getter private WorldGuardSupport worldGuardSupport;

    @Getter private ProfileManager profiles;
    @Getter private MenuManager menuManager;
    
    @Getter private BaseCommand baseCommand;
    @Getter private AdminCommand adminCommand;
 
    @Getter private AchievementsExecutor achievementManager;

    private VolatileCodeHandler volatileCodeHandler;

    /*
     * Other Shit
     */ 
    @Getter private Boolean isUpdateAvailable = false;
    
    /** 
     * Bukkit Loader
     * @author Ashijin
     * @exclude
     */
    @Override
    public void load() {

        plugin = this;

        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardSupport = new WorldGuardSupport(this);
        }
    }  
    
    /**
     * Bukkit Initializer
     * @author Ashijin
     * @exclude
     */
    @Override 
    public void enable() {
        MCLogger.log("Loading {0} for {1} {2}...", getDescription().getName(), ServerVersion.isPaper() ? "Paper" : "Spigot", ServerVersion.get().toString());

        if(ServerVersion.isPaper()) {
            MCLogger.log("The server is running PaperSpigot; enabled PaperSpigot exclusive functionality");
        } else {
            MCLogger.log("The server is running Spigot; disabled PaperSpigot exclusive functionality");
        }

        var categoriesFile = new File(getDataFolder(), "categories.yml");
        if(!categoriesFile.exists()) {
            saveResource("categories.yml", false);
        }
        
        this.saveDefaultConfig();
        
        configuration = new Configuration(this);
        
        getConfiguration().load(this);
        MCLogger.log("MythicAchievements configuration file loaded successfully.");

        /*
         * Plugin Components
         */

        //volatileCodeHandler = getVolatileCodeHandler();
        compatibility = new CompatibilityManager(this);
        
        achievementManager = new AchievementsExecutor(this);
        
        profiles = new ProfileManager(this); 
        
        menuManager = new MenuManager(this);
        
        /*
         * Events
         */
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);

        /*
         * Commands 
         */
        this.baseCommand = new BaseCommand(this);
        this.adminCommand = new AdminCommand(this);

        //this.registerCommand("achievements", baseCommand);
        this.registerCommand("mythicachievements", adminCommand);

        if(configuration.isAllowingMetrics())  {
            new bStats(this);
        }

        MCLogger.log("" + ConsoleColor.GREEN + ConsoleColor.CHECK_MARK + " MythicAchievements" + (isPremium() ? " Premium" : "") + " v" + getVersion() +  " (build "+ getBuildNumber() +") has been successfully loaded!" + ConsoleColor.RESET);
    }

    @Override
    /**
     * Bukkit Shutdown
     * @author Ashijin
     * @exclude
     */
    public void disable() {
        MCLogger.log("Disabling MythicAchievements...");

        profiles.unload();
        configuration.unload();
        compatibility.terminate(); 
        
        MCLogger.log("All active settings have been saved.");
    }
    
    /**
     * @return MythicAchievements Returns the active MythicAchievements instance.
     */
    public static MythicAchievementsPlugin inst()    {
        return plugin; 
    } 
    
    /** 
     * @exclude
     */
    private static Object p = amIPremium();

    /**
     * @exclude 
     */
    public static final boolean isPremium() {
        return p != null;
    }
    
    /**
     * @exclude 
     */
    private static Object amIPremium() {
        try {
            return Class.forName("io.lumine.achievements.CarsonJF");
        } catch (final ClassNotFoundException e) {
            return null;
        } catch (final Exception e) {
            throw new RuntimeException("An error occurred while enabling CarsonJF", e);
        }
    }

    /**
     * Grabs the active VolatileCodeHandler for the current version of Minecraft
     * @return {@link VolatileCodeHandler}
     */
    public VolatileCodeHandler getVolatileCodeHandler()  {
        if(this.volatileCodeHandler != null) return this.volatileCodeHandler;
        
        VolatileCodeHandler VCH = new VolatileCodeDisabled();
                
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        if (version.equals("craftbukkit")) {
            version = "pre";
        }   
        try {
            final Class<?> clazz = Class.forName("io.lumine.cosmetics.nms.VolatileCodeEnabled_" + version);
            if (VolatileCodeHandler.class.isAssignableFrom(clazz)) {
                VCH = (VolatileCodeHandler) clazz.getConstructor(MythicAchievementsPlugin.class).newInstance(this);
            }
        } catch (final ClassNotFoundException e) {  
            MCLogger.error(ColorString.get("&6--====|||| &c&lMythicAchievements &6||||====--"));
            MCLogger.error("This version of MythicAchievements is not fully compatible with your version of Bukkit.");
            MCLogger.error("Some features may be limited or disabled until you use a compatible version.");  
        } catch (final Exception e) {
            throw new RuntimeException("Unknown exception loading version handler. Volatile code has been disabled.", e);
        }
        this.volatileCodeHandler = VCH;
        return VCH;
    }
    
    /**
     * Returns the plugin version
     * @return int
     */
    public String getVersion()  {
        return this.getDescription().getVersion().split("-")[0];
    }
 
    /**
     * Returns the development build version
     * @return int
     */
    public String getBuildNumber()  {
        final String[] split = this.getDescription().getVersion().split("-");
        if(split.length == 2)   {
            return split[1];
        } else if(split.length == 3)    {
            return split[2];
        } else  {
            return "????";
        }
    }
}
