package io.lumine.achievements.achievement.criteria;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.mythic.bukkit.utils.menu.Icon;

public class ManualCriteria extends Criteria {

    public ManualCriteria(Achievement holder) {
        super(holder);
    }

    @Override
    public Icon<AchievementProfile> getIcon() {
        return null;
    }

}
