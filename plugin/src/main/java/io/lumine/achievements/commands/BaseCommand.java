package io.lumine.achievements.commands;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.constants.Permissions;
import io.lumine.mythic.bukkit.utils.commands.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class BaseCommand extends Command<MythicAchievementsPlugin> {

    public BaseCommand(MythicAchievementsPlugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        final var player = (Player) sender;
        final var profile = getPlugin().getProfiles().getProfile(player);
        
        if(profile == null) {
            throw new IllegalStateException("This should never happen, please report to the developers.");
        }
        
        getPlugin().getMenuManager().getCategoriesMenu().openMenu(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_BASE;
    }

    @Override
    public boolean isConsoleFriendly() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }
}
