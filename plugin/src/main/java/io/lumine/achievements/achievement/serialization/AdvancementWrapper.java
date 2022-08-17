package io.lumine.achievements.achievement.serialization;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.lumine.achievements.achievement.AchievementImpl;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.constants.Constants;

public class AdvancementWrapper {
   
    private final Map<String,MythicAdvancementCriteria> criteria = Maps.newConcurrentMap();
    private final Collection<String[]> requirements = Lists.newArrayList();
    private MythicAdvancementDisplay display;
    private String parent;
    
    public AdvancementWrapper(AchievementImpl achieve) {
        criteria.put(Constants.CRITERIA_KEY, new MythicAdvancementCriteria());
        requirements.add(new String[] { Constants.CRITERIA_KEY });
        
        this.display = new MythicAdvancementDisplay(achieve);
        
        if(achieve.getParent().isPresent()) {
            this.parent = achieve.getParent().get().getNamespacedKey().toString();
        } else {
            this.parent = achieve.getCategory().getNamespacedKey().toString();
        }
    }
    
    private class MythicAdvancementCriteria {
        private final String trigger = "minecraft:impossible";
    }

    private class MythicAdvancementDisplay {
        
        private MythicAdvancementIcon icon;
        private String title;
        private String description;
        private String frame;
        private boolean announce_to_chat;
        
        public MythicAdvancementDisplay(AchievementImpl achieve) {
            this.icon = new MythicAdvancementIcon(achieve);
            this.title = achieve.getTitle();
            this.description = achieve.getDescription();
            this.frame = achieve.getFrame().key();
        }
    }
    
    private class MythicAdvancementIcon {
        
        private final String item;
        private final String nbt;
        
        public MythicAdvancementIcon(AchievementImpl achieve) {
            this.item = achieve.getIconMaterial().name().toLowerCase();
            this.nbt = achieve.getIconNBT();
            
        }
    }
    
}
