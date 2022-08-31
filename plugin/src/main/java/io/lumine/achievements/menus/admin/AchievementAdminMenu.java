package io.lumine.achievements.menus.admin;

import org.bukkit.entity.Player;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.MenuManager;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.mythic.bukkit.utils.config.properties.types.MenuProp;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;
import io.lumine.mythic.bukkit.utils.menu.ReloadableMenu;
import lombok.Getter;

public abstract class AchievementAdminMenu<T> extends ReloadableMenu<T> {

    @Getter protected MythicAchievementsPlugin plugin;
    @Getter protected MenuManager menuManager;

    public AchievementAdminMenu(MythicAchievementsPlugin core, MenuManager manager, String menuFile) {
        super(new MenuProp(core,core.getPropertyFileInternal(core.getClass().getResourceAsStream(menuFile)), "Menu", null));
        this.plugin = core;
        this.menuManager = manager;
    }
    
    public AchievementAdminMenu(MythicAchievementsPlugin core, MenuManager manager, String menuFile, boolean buildOnOpen) {
        super(new MenuProp(core,core.getPropertyFileInternal(core.getClass().getResourceAsStream(menuFile)), "Menu", null), buildOnOpen);
        this.plugin = core;
        this.menuManager = manager;
    }

    public EditableMenuBuilder<ProfileImpl> addPageButtons(EditableMenuBuilder<ProfileImpl> builder) {
        builder.getIcon("NEXT_PAGE").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                playMenuClick(player);
                nextPage(player);
            });
        });
        builder.getIcon("PREVIOUS_PAGE").ifPresent(icon -> {
            icon.getBuilder().click((profile,player) -> {
                playMenuClick(player);
                previousPage(player);
            });
        });
        return builder;
    }
    
    public static void playMenuClick(Player player) {
        player.playSound(player.getLocation(), "entity.chicken.egg", 1F, 1F);
    }
}