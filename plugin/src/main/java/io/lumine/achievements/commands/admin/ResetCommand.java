package io.lumine.achievements.commands.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.AchievementsExecutor;
import io.lumine.achievements.commands.CommandHelper;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.constants.Permissions;
import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.config.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResetCommand extends Command<MythicAchievementsPlugin> {

    public ResetCommand(AdminCommand plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        var player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            CommandHelper.sendError(sender, "Player not found");
            return true;
        }
        
        var profile = getPlugin().getProfiles().getProfile(player);

        profile.resetAchievements();
        CommandHelper.sendSuccess(sender, "Reset achievements for player <yellow>" + player.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> players = new ArrayList<>();
            
            for(Player p : Bukkit.getOnlinePlayers())   {
                players.add(p.getName());
            }
            return StringUtil.copyPartialMatches(args[0], players, new ArrayList<>());
        } else {
            return Collections.emptyList();
        }
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
        return "reset";
    }
}