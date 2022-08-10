package io.lumine.achievements.menus.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class AchievementBrowserMenu extends AchievementAdminMenu<Profile> {

    public AchievementBrowserMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, "/menus/achievement-browser.yml");
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        return builder;
    }

}
