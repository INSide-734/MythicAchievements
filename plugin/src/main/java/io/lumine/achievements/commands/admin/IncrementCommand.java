package io.lumine.achievements.commands.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.AchievementsExecutor;
import io.lumine.achievements.achievement.criteria.ManualCriteria;
import io.lumine.achievements.commands.CommandHelper;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.constants.Permissions;
import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.config.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncrementCommand extends Command<MythicAchievementsPlugin> {

    public IncrementCommand(AdminCommand plugin) {
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
        
        if(achieve.getCriteria() instanceof ManualCriteria manualCriteria) {
            if(manualCriteria.checkConditions(player)) {
                manualCriteria.incrementStat(player);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            return null;
        }
        if(args.length == 2) {
            var player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                return Collections.emptyList();
            }
            var profile = getPlugin().getProfiles().getProfile(player);
            return StringUtil.copyPartialMatches(args[0], profile.getCompletedAchievementNames(), new ArrayList<>());
        }
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
        return "increment";
    }
}