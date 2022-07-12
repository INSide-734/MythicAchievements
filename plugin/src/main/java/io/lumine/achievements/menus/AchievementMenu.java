package io.lumine.achievements.menus;

import org.bukkit.entity.Player;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.api.MythicAchievements;
import io.lumine.achievements.players.Profile;
import io.lumine.mythic.bukkit.utils.config.properties.types.MenuProp;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;
import io.lumine.mythic.bukkit.utils.menu.ReloadableMenu;
import lombok.Getter;

public abstract class AchievementMenu<T> extends ReloadableMenu<T> {

    @Getter protected MythicAchievementsPlugin plugin;
    @Getter protected MenuManager menuManager;
    
    public AchievementMenu(MythicAchievementsPlugin core, MenuManager manager, MenuProp menu) {
        super(menu);
        this.plugin = core;
        this.menuManager = manager;
    }
    
    public AchievementMenu(MythicAchievementsPlugin core, MenuManager manager, MenuProp menu, boolean buildOnOpen) {
        super(menu, buildOnOpen);
        this.plugin = core;
        this.menuManager = manager;
    }
    
    public void openMenu(Player player) {
        Profile profile = plugin.getProfiles().getProfile(player);

        //boolean b = profile.getHatIsActive();
        
        //profile.setHatIsActive(false);
        //MainMenu.playMenuClick(player);
        //menu.open(player, state);
        //profile.setHatIsActive(b);
    }
    
    public EditableMenuBuilder<Profile> addPageButtons(EditableMenuBuilder<Profile> builder) {
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