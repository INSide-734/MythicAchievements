package io.lumine.achievements.api.achievements;

import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;

public interface AchievementNode {

    public NamespacedKey getNamespacedKey();

    public Advancement getAdvancement();
    
    public void setAdvancement(Advancement adv);
    
    public String getTitle();
    
    public String getDescription();
    
    public AchievementFrame getFrame();
    
    public boolean isHidden();
    
}
