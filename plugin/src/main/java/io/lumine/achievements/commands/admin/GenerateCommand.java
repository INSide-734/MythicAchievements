package io.lumine.achievements.commands.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.AchievementsExecutor;
import io.lumine.achievements.commands.CommandHelper;
import io.lumine.achievements.constants.Permissions;
import io.lumine.achievements.nms.VolatileCodeDisabled;
import io.lumine.mythic.bukkit.utils.chat.ColorString;
import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.version.ServerVersion;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class GenerateCommand extends Command<MythicAchievementsPlugin> {

    public GenerateCommand(AdminCommand plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        ((AchievementsExecutor) getPlugin().getAchievementManager()).getAdvancementGUIManager().registerAdvancements();
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
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
        return "generate";
    }
}