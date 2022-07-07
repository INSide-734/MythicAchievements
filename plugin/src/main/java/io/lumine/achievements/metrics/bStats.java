package io.lumine.achievements.metrics;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.logging.MCLogger;

public class bStats {

	public bStats(MythicAchievementsPlugin plugin)	{
		try {
        	Metrics metrics = new Metrics(plugin, 15050);
        	
            final String devBuilds = plugin.getDescription().getVersion().contains("SNAPSHOT") ? "Yes" : "No";
            final String preBuilds = MythicAchievementsPlugin.isPremium() ? "Yes" : "No";
            
            metrics.addCustomChart(new SimplePie("premium", () -> preBuilds));
            metrics.addCustomChart(new SimplePie("devbuilds", () -> devBuilds));
            
            MCLogger.log("Started up bStats Metrics");
        } catch (Exception e) {
            MCLogger.error("Metrics: Failed to enable bStats Metrics stats.");
        }
	}
}
