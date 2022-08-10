package io.lumine.achievements.menus.admin;

import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import lombok.Data;

@Data
public class CategoryEditorMenuContext {

    private final MythicItem item;
    private final CategoryMenuContext previousMenu;
    
    public void openMenu(Player player) {
        //MythicBukkit.inst().getMenuManager().getItemEditorMenu().open(player, this);
    }
    
    public void openPreviousMenu(Player player) {
        //MythicBukkit.inst().getMenuManager().getItemBrowseMenu().open(player, previousMenu);
    }
}
