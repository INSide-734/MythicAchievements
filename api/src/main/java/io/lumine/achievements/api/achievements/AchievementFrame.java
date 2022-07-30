package io.lumine.achievements.api.achievements;

public enum AchievementFrame {

    CHALLENGE,
    GOAL,
    TASK;
    
    public String key() {
        return toString().toLowerCase();
    }
}
