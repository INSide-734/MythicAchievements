package io.lumine.achievements.menus;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.menus.admin.*;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;
import lombok.Getter;

public class MenuManager extends ReloadableModule<MythicAchievementsPlugin> {

    @Getter private CategoryMenu categoriesMenu;
    @Getter private AchievementsMenu achievementsMenu;
    
    @Getter private AdminMenu adminMenu;
    @Getter private AchievementBrowserMenu adminAchievementBrowserMenu;
    @Getter private AchievementEditorMenu adminAchievementEditorMenu;
    @Getter private AchievementCriteriaSelectionMenu adminAchievementCriteriaSelectionMenu;
    @Getter private AchievementCriteriaEditorMenu adminAchievementCriteriaEditorMenu;
    @Getter private CategoryBrowserMenu adminCategoryBrowserMenu;
    @Getter private CategoryEditorMenu adminCategoryEditorMenu;
    @Getter private SettingsEditorMenu adminSettingsMenu;
    
    public MenuManager(MythicAchievementsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {

        if(achievementsMenu == null) {
            categoriesMenu = new CategoryMenu(plugin,this);
            achievementsMenu = new AchievementsMenu(plugin,this);
            
            adminMenu = new AdminMenu(plugin,this);
            adminAchievementBrowserMenu = new AchievementBrowserMenu(plugin,this);
            adminAchievementEditorMenu = new AchievementEditorMenu(plugin,this);
            adminAchievementCriteriaSelectionMenu = new AchievementCriteriaSelectionMenu(plugin,this);
            adminAchievementCriteriaEditorMenu = new AchievementCriteriaEditorMenu(plugin,this);
            adminCategoryBrowserMenu = new CategoryBrowserMenu(plugin,this);
            adminCategoryEditorMenu = new CategoryEditorMenu(plugin,this);
            adminSettingsMenu = new SettingsEditorMenu(plugin,this);
        }
        
        categoriesMenu.reload();
        achievementsMenu.reload();
        
        adminMenu.reload();
        adminAchievementBrowserMenu.reload();
        adminAchievementEditorMenu.reload();
        adminAchievementCriteriaSelectionMenu.reload();
        adminAchievementCriteriaEditorMenu.reload();
        adminCategoryBrowserMenu.reload();
        adminCategoryEditorMenu.reload();
        adminSettingsMenu.reload();
    }

    @Override
    public void unload() {}

}
