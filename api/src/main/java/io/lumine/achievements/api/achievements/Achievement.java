package io.lumine.achievements.api.achievements;

import java.util.Optional;

import io.lumine.achievements.api.achievements.manager.AchievementManager;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.utils.config.properties.PropertyHolder;
import io.lumine.utils.menu.MenuData;
import lombok.Getter;

public abstract class Achievement implements PropertyHolder,MenuData<AchievementProfile> {
 
    @Getter private final AchievementManager manager;
    @Getter private final String type;
    @Getter private final String key;
    
    public Achievement(AchievementManager manager, String type, String key) {
        this.manager = manager;
        this.type = type;
        this.key = key;
    }

    public boolean has(AchievementProfile profile) {
        return profile.has(this);
    }

    public abstract String getId();
    
    public abstract String getNamespace();
    
}
