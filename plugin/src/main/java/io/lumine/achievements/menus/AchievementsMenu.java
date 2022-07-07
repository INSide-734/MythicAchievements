package io.lumine.achievements.menus;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.players.Profile;
import io.lumine.utils.config.properties.types.MenuProp;
import io.lumine.utils.menu.EditableMenuBuilder;

public class AchievementsMenu extends AchievementMenu<Profile> {

    public AchievementsMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/achievements", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<Profile> build(EditableMenuBuilder<Profile> builder) {

        return builder;
    }

}
