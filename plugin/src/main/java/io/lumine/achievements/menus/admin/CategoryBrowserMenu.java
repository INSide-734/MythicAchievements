package io.lumine.achievements.menus.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class CategoryBrowserMenu extends AchievementAdminMenu<ProfileImpl> {

    public CategoryBrowserMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, "/menus/category-browser.yml");
    }

    @Override
    public EditableMenuBuilder<ProfileImpl> build(EditableMenuBuilder<ProfileImpl> builder) {

        return builder;
    }

}
