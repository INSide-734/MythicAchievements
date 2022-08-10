package io.lumine.achievements.menus.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class AchievementCriteriaSelectionMenu extends AchievementAdminMenu<Profile> {

    public AchievementCriteriaSelectionMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, "/menus/achievement-criteria-selection.yml");
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        return builder;
    }

}
