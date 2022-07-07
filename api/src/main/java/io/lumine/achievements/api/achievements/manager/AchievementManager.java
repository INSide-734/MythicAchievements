package io.lumine.achievements.api.achievements.manager;

import io.lumine.achievements.api.players.AchievementProfile;

public interface AchievementManager {

    void equip(AchievementProfile profile);
    void unequip(AchievementProfile profile);
    
}
