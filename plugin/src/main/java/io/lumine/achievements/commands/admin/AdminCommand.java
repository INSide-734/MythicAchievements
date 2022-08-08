package io.lumine.achievements.commands.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.commands.CommandHelper;
import io.lumine.achievements.constants.Permissions;
import io.lumine.mythic.bukkit.utils.adventure.text.Component;
import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.text.Text;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class AdminCommand extends Command<MythicAchievementsPlugin> {

    public AdminCommand(MythicAchievementsPlugin plugin) {
        super(plugin);
        
        addSubCommands(
                new GenerateCommand(this),
                new ReloadCommand(this),
                new VersionCommand(this),
                new GrantCommand(this),
                new RevokeCommand(this),
                new IncrementCommand(this),
                new ResetCommand(this));
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        Component[] messages = {

                Text.parse("&e/mythicachievements &aincrement [name] [achievement] &7\u25BA &7&oIncrement a manual achievement"),
                Text.parse("&e/mythicachievements &agrant [name] [achievement] &7\u25BA &7&oGrants an achievement"),
                Text.parse("&e/mythicachievements &arevoke [name] [achievement] &7\u25BA &7&oRevokes an achievement"),
                Text.parse("&e/mythicachievements &areset [name] &7\u25BA &7&oResets a player's achievement"),
                
                Text.parse("&e/mythicachievements &areload &7\u25BA &7&oReloads everything"),
                Text.parse("&e/mythicachievements &aversion &7\u25BA &7&oPlugin version information")
              };
        CommandHelper.sendCommandMessage(sender, messages);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_ADMIN;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
