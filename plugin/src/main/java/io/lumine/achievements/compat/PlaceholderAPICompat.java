package io.lumine.achievements.compat;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.players.Profile;

public class PlaceholderAPICompat extends PlaceholderExpansion {

    private final MythicAchievementsPlugin plugin;

    public PlaceholderAPICompat(MythicAchievementsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        Profile profile = plugin.getProfiles().getProfile(player);

        switch (identifier) {
            default:
                return null;
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "mythicachievements";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Lumine Studios";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
}
