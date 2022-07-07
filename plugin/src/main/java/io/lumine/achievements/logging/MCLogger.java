package io.lumine.achievements.logging;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.utils.logging.ConsoleColor;

import java.util.logging.Level;

public class MCLogger {

    private static MythicAchievementsPlugin getPlugin() {
        return MythicAchievementsPlugin.inst();
    }
    
	public static void log(String message) {
	    getPlugin().getLogger().log(Level.INFO, message + ConsoleColor.RESET);
    }

	public static void log(String message, Object... params) {
	    getPlugin().getLogger().log(Level.INFO, message + ConsoleColor.RESET, params);
    }
	
	public static void log(Level level, String message) {
	    getPlugin().getLogger().log(level, message + ConsoleColor.RESET);
    }
	
    public static void log(Level level, String message, Object... params) {
        getPlugin().getLogger().log(level, message + ConsoleColor.RESET, params);
    }

    public static void error(String message) {
        log(Level.WARNING, message + ConsoleColor.RESET);
    }
    
    public static void error(String message, Object... params) {
        log(Level.WARNING, message + ConsoleColor.RESET, params);
    }
    
    public static void error(String message, Throwable ex) {
        log(Level.SEVERE, message + ConsoleColor.RESET);
        log(Level.SEVERE, "    " + ex.getMessage() + " (" + ex.getClass().getName() + ")");

        ex.printStackTrace();
    }
    
    public static void error(String message, Throwable ex, Object... params) {
        log(Level.SEVERE, message + ConsoleColor.RESET, params);
        log(Level.SEVERE, "    " + ex.getMessage() + " (" + ex.getClass().getName() + ")");

        ex.printStackTrace();
    }
	
    private MCLogger() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }
}
