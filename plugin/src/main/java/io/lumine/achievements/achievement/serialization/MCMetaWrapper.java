package io.lumine.achievements.achievement.serialization;

public class MCMetaWrapper {

    private final MCMetaPackData pack = new MCMetaPackData();
    
    private class MCMetaPackData {
        
        private final int pack_format = 6;
        private final String description = "Pack generated by MythicAchievements";
        
    }
}