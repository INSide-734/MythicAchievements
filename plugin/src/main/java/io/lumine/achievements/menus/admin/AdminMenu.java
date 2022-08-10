package io.lumine.achievements.menus.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class AdminMenu extends AchievementAdminMenu<Profile> {

    public AdminMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, "/menus/main.yml");
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        builder.getIcon("BUTTON_ACHIEVEMENTBROWSER").ifPresent((icon) -> {
            icon.getBuilder().click((profile,player) -> {
                //getMenuManager().getAdminCategoryBrowserMenu().openMenu(player);
            });
        });
        
        return builder;
    }

}
