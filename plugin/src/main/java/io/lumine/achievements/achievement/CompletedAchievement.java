package io.lumine.achievements.achievement;

import io.lumine.achievements.api.achievements.Achievement;
import lombok.Data;

@Data
public class CompletedAchievement {

    private final long timestamp;
    private boolean rewardClaimed = false;
    
    public CompletedAchievement(Achievement achieve) {
        this.timestamp = System.currentTimeMillis();
    }
}
