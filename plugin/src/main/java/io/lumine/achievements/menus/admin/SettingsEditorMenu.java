package io.lumine.achievements.menus.admin;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class SettingsEditorMenu extends AchievementAdminMenu<Profile> {

    public SettingsEditorMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, "/menus/settings.yml");
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        return builder;
    }

}
