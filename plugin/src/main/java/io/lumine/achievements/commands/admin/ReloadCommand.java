package io.lumine.achievements.commands.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.achievement.AchievementsExecutor;
import io.lumine.achievements.commands.CommandHelper;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.constants.Permissions;
import io.lumine.mythic.bukkit.utils.commands.Command;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command<MythicAchievementsPlugin> {

    public ReloadCommand(AdminCommand plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        getPlugin().reloadConfiguration();

        getPlugin().getMenuManager().reload();
        getPlugin().getAchievementManager().reload();
        
        ((AchievementsExecutor) getPlugin().getAchievementManager()).getAdvancementGUIManager().registerAdvancements();
        
        getPlugin().getProfiles().getKnownProfiles().forEach(profile -> profile.rebuildAchievements());
        
        CommandHelper.sendSuccess(sender, Property.String(Scope.CONFIG,
                "Configuration.Language.Reloaded","MythicAchievements has been reloaded.").get());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getPermissionNode() {
        return Permissions.COMMAND_RELOAD;
    }

    @Override
    public boolean isConsoleFriendly() {
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }
}