package io.lumine.achievements.compat;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.compat.mythicmobs.*;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.utils.Events;

public class MythicMobsCompat {
    
    private final MythicAchievementsPlugin plugin;
    private final MythicBukkit mythicMobs;

    public MythicMobsCompat(MythicAchievementsPlugin plugin) {
        this.plugin = plugin;
        this.mythicMobs = MythicBukkit.inst();

        Events.subscribe(MythicMechanicLoadEvent.class).handler(event -> {
            switch(event.getMechanicName().toUpperCase()){
                case "GRANTACHIEVEMENT": case "GIVEACHIEVEMENT":
                    event.register(new GrantAchievementMechanic(plugin, event.getConfig()));
                    break;
                default:
            }
        }).bindWith(plugin);
        
        Events.subscribe(MythicConditionLoadEvent.class).handler(event -> {
            switch(event.getConditionName().toUpperCase()){
                case "HASACHIEVEMENT":
                    event.register(new HasAchievementCondition(plugin, event.getConfig()));
                    break;
                default:
            }
        }).bindWith(plugin);

    }
}
