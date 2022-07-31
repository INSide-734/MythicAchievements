package io.lumine.achievements.config;

import java.io.File;

import io.lumine.achievements.MythicAchievementsPlugin;
import io.lumine.achievements.api.MythicAchievements;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.plugin.ReloadableModule;

public class Language extends ReloadableModule<MythicAchievementsPlugin> {

    private static final StringProp LANG = Property.String(Scope.CONFIG, "Configuration.General.Language", "english-us");
    
    private String language;
    private File languageFile;
    
    public Language(MythicAchievementsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void load(MythicAchievementsPlugin plugin) {
        this.language = LANG.get();
        
        
    }

    @Override
    public void unload() {
        // TODO Auto-generated method stub
        
    }

}
