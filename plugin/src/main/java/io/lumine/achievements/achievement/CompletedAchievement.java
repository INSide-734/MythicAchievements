package io.lumine.achievements.achievement;

import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.storage.sql.jooq.tables.records.ProfileCompletedRecord;
import io.lumine.achievements.storage.sql.jooq.tables.records.ProfileProgressRecord;
import lombok.Data;

@Data
public class CompletedAchievement {

    private final long timestamp;
    private boolean rewardClaimed = false;
    
    public CompletedAchievement(Achievement achieve) {
        this.timestamp = System.currentTimeMillis();
    } 
    
    public CompletedAchievement(ProfileCompletedRecord record) {
        this.timestamp = record.getCompletedTime();
        this.rewardClaimed = record.getCollectedLoot() == 1 ? true : false;
    }
}
