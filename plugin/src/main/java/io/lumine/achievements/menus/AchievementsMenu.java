package io.lumine.achievements.menus;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.bukkit.utils.config.properties.types.MenuProp;
import io.lumine.mythic.bukkit.utils.logging.Log;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class AchievementsMenu extends AchievementMenu<Profile> {

    public AchievementsMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/achievements", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        return builder;
    }

    public void openMenu(Player player, AchievementCategory category) {
        var profile = plugin.getProfiles().getProfile(player);
        
        List<Achievement> achieves = category.getAchievements();
        Log.info("Opening menu with {0} achievements", achieves.size());
        this.open(player, profile, achieves);
    }
}
