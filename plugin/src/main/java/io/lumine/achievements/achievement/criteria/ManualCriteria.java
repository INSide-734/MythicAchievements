package io.lumine.achievements.achievement.criteria;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.utils.annotations.MythicAchievementCriteria;
import io.lumine.mythic.bukkit.utils.menu.Icon;

@MythicAchievementCriteria(name="manual", aliases={})
public class ManualCriteria extends Criteria {

    public ManualCriteria(String criteriaNode, Achievement holder) {
        super(criteriaNode, holder);
    }

    public void load() {}

    @Override
    public Icon<AchievementProfile> getIcon() {
        return null;
    }

}
