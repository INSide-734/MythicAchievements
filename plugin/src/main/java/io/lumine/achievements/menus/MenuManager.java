package io.lumine.achievements.menus;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import lombok.Getter;

public class MenuManager extends ReloadableModule<MythicAchievementsPlugin> {

    @Getter private CategoryMenu categoriesMenu;
    @Getter private AchievementsMenu achievementsMenu;
    
    public MenuManager(MythicAchievementsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {

        if(achievementsMenu == null) {
            categoriesMenu = new CategoryMenu(plugin,this);
            achievementsMenu = new AchievementsMenu(plugin,this);
        }
        
        categoriesMenu.reload();
        achievementsMenu.reload();
    }

    @Override
    public void unload() {}

}
