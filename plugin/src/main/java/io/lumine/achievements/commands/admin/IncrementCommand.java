package io.lumine.achievements.commands.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.AchievementsExecutor;
import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.achievement.criteria.ManualCriteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCriteria;
import io.lumine.achievements.commands.CommandHelper;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.constants.Permissions;
import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.config.properties.Property;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        
        var criteriaName = args[2];
        
        AchievementCriteria found = null;
        for(var criteria : achieve.getCriteria()) {
            if(criteria.getKey().equalsIgnoreCase(criteriaName)) {
                found = criteria;
                break;
            }
        }
        if(found == null) {
            CommandHelper.sendError(sender, "Criteria not found");
            return true;
        }
        
        if(found instanceof ManualCriteria manualCriteria) {
            if(manualCriteria.checkConditions(player)) {
                manualCriteria.incrementStat(player);
            }
        } else {
            ((Criteria) found).incrementStat(player);
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
            
            Collection<String> subscribedNames = profile.getSubscribedAchievements().stream().map(achieve -> achieve.getKey()).collect(Collectors.toList());
            
            return StringUtil.copyPartialMatches(args[1], subscribedNames, new ArrayList<>());
        }
        if(args.length == 3) {
            var player = Bukkit.getPlayer(args[0]);
            if(player == null) {
                return Collections.emptyList();
            }
            var profile = getPlugin().getProfiles().getProfile(player);
            
            Achievement selected = null;
            for(var achieve : profile.getSubscribedAchievements()) {
                if(achieve.getKey().equalsIgnoreCase(args[1])) {
                    selected = achieve;
                    break;
                }
            }
            if(selected == null) {
                return Collections.EMPTY_LIST;
            }
            
            Collection<String> criteriaNames = selected.getCriteria().stream().map(c -> c.getKey()).collect(Collectors.toList());
            
            return StringUtil.copyPartialMatches(args[2], criteriaNames, new ArrayList<>());
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