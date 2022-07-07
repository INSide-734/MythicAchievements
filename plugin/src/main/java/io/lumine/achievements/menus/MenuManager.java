package io.lumine.achievements.menus;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.utils.plugin.ReloadableModule;
import lombok.Getter;

public class MenuManager extends ReloadableModule<MythicAchievementsPlugin> {

    @Getter private AchievementsMenu achievementsMenu;
    
    public MenuManager(MythicAchievementsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {

        if(achievementsMenu == null) {
            achievementsMenu = new AchievementsMenu(plugin,this);
        }
        
        achievementsMenu.reload();
    }

    @Override
    public void unload() {}

}
