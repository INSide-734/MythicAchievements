package io.lumine.achievements.achievement;

import lombok.Getter;

public enum VanillaAchievements {

    STORY("story"),
    ADVENTURE("adventure"),
    HUSBANDRY("husbandry"),
    END("end"),
    NETHER("nether")
    
    ;
    
    @Getter private final String folder;
    
    private VanillaAchievements(String folder) {
        this.folder = folder;
    }

}
