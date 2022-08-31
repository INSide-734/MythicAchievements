package io.lumine.achievements.menus;

import java.util.List;

import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.api.achievements.AchievementCategory;
import io.lumine.achievements.players.ProfileImpl;
import io.lumine.mythic.bukkit.utils.config.properties.types.MenuProp;
import io.lumine.mythic.bukkit.utils.menu.EditableMenuBuilder;

public class CategoryMenu extends AchievementMenu<ProfileImpl> {

    public CategoryMenu(MythicAchievementsPlugin core, MenuManager manager) {
        super(core, manager, new MenuProp(core, "menus/categories", "Menu", null));
    }

    @Override
    public EditableMenuBuilder<ProfileImpl> build(EditableMenuBuilder<ProfileImpl> builder) {

        return builder;
    }
    
    public void openMenu(Player player) {
        var profile = plugin.getProfiles().getProfile(player);
        
        List<AchievementCategory> categories = Lists.newArrayList(getPlugin().getAchievementManager().getCategories());
        
        this.open(player, profile, categories);
    }

}
