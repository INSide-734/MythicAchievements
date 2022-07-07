package io.lumine.achievements.managers;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.utils.plugin.ReloadableModule;

public class AchievementsExecutor extends ReloadableModule<MythicAchievementsPlugin> {

    public AchievementsExecutor(MythicAchievementsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {}

    @Override
    public void unload() {
        
    }
    
}
