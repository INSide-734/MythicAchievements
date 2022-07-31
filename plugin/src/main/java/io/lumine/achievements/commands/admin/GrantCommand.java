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

public class GrantCommand extends Command<MythicAchievementsPlugin> {

    public GrantCommand(AdminCommand plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {

        var player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            CommandHelper.sendError(sender, "Player not found");
            return true;
        }
        
        var maybeAchieve = getPlugin().getAchievementManager().getAchievement(args[1]);
        if(maybeAchieve.isEmpty()) {
            CommandHelper.sendError(sender, "Achievement not found");
            return true;
        }
        var achieve = maybeAchieve.get();
        
        var profile = getPlugin().getProfiles().getProfile(player);
        if(profile.hasCompleted(achieve)) {
            CommandHelper.sendError(sender, "Player has already completed that achievement");
            return true;
        }
        
        profile.completeAchievement(achieve, false);
        
        CommandHelper.sendSuccess(sender, "Granted achievement to player");
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
        } else if(args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], getPlugin().getAchievementManager().getAchievementNames(), new ArrayList<>());
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
        return "grant";
    }
}