package io.lumine.achievements.achievement.serialization;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.lumine.achievements.achievement.AchievementCategoryImpl;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.constants.Constants;

public class AdvancementCategoryWrapper {

    private final Map<String,MythicAdvancementCriteria> criteria = Maps.newConcurrentMap();
    private final Collection<String[]> requirements = Lists.newArrayList();
    private MythicAdvancementDisplay display;

    public AdvancementCategoryWrapper(AchievementCategoryImpl achieve) {
        criteria.put(Constants.CRITERIA_KEY, new MythicAdvancementCriteria());
        requirements.add(new String[] { Constants.CRITERIA_KEY });
        
        this.display = new MythicAdvancementDisplay(achieve);
    }
    
    private class MythicAdvancementCriteria {
        private final String trigger = "minecraft:impossible";
    }

    private class MythicAdvancementDisplay {
        
        private MythicAdvancementIcon icon;
        private String title;
        private String description;        
        private String background;
        private String frame;
        private boolean show_toast = false;
        private boolean announce_to_chat = false;
        
        public MythicAdvancementDisplay(AchievementCategoryImpl category) {
            this.icon = new MythicAdvancementIcon(category);
            this.title = category.getTitle();
            this.description = category.getDescription();
            this.background = category.getBackground();
            this.frame = category.getFrame().key();
        }
    }
    
    private class MythicAdvancementIcon {
        
        private final String item;
        //private final int data;
        
        public MythicAdvancementIcon(AchievementCategoryImpl category) {
            this.item = category.getIconMaterial().name().toLowerCase();
            //this.data = category.getIconData();
        }
    }
    
}
