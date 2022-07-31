package io.lumine.achievements.compat;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.compat.mythicmobs.GrantAchievementMechanic;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.mythic.bukkit.utils.Events;


import java.util.Locale;

public class MythicMobsCompat {
    private final MythicAchievementsPlugin plugin;
    private final MythicBukkit mythicMobs;

    public MythicMobsCompat(MythicAchievementsPlugin plugin) {
        this.plugin = plugin;
        this.mythicMobs = MythicBukkit.inst();

        Events.subscribe(MythicMechanicLoadEvent.class).handler(event -> {
            switch(event.getMechanicName().toUpperCase()){
                case "GRANTACHIEVEMENT":
                    event.register(new GrantAchievementMechanic(plugin, event.getConfig()));
            }
        }).bindWith(plugin);

    }
}
