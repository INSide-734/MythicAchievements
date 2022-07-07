package io.lumine.achievements.compat;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.compat.mythicmobs.CosmeticEmoteMechanic;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.utils.Events;


import java.util.Locale;

public class MythicMobsCompat {
    private final MythicAchievementsPlugin plugin;
    private final MythicBukkit mythicMobs;

    public MythicMobsCompat(MythicAchievementsPlugin plugin) {
        this.plugin = plugin;
        this.mythicMobs = MythicBukkit.inst();

        Events.subscribe(MythicMechanicLoadEvent.class).handler(event -> {
            switch(event.getMechanicName().toUpperCase()){
                case "COSMETICEMOTE":
                    event.register(new CosmeticEmoteMechanic(event.getMechanicName(), event.getConfig()));
            }
        }).bindWith(plugin);

    }
}
