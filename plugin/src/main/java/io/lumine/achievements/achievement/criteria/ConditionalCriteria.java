package io.lumine.achievements.achievement.criteria;

import io.lumine.achievements.achievement.Criteria;
import io.lumine.achievements.api.achievements.Achievement;
import io.lumine.achievements.api.players.AchievementProfile;
import io.lumine.achievements.config.Scope;
import io.lumine.achievements.utils.annotations.MythicAchievementCriteria;
import io.lumine.mythic.bukkit.utils.Schedulers;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.BooleanProp;
import io.lumine.mythic.bukkit.utils.config.properties.types.IntProp;
import io.lumine.mythic.bukkit.utils.menu.Icon;

@MythicAchievementCriteria(name="conditional", aliases={"timer","condition"})
public class ConditionalCriteria extends Criteria implements Runnable {

    private final IntProp CHECK_INTERVAL = Property.Int(Scope.NONE, "CheckInterval", 20);
    private final BooleanProp SYNC = Property.Boolean(Scope.NONE, "Sync", false);
    
    private final int checkInterval;
    private final boolean sync;
    
    public ConditionalCriteria(String criteriaNode, Achievement holder) {
        super(criteriaNode, holder);
        
        this.checkInterval = CHECK_INTERVAL.fget(holder.getFile(), this);
        this.sync = SYNC.fget(holder.getFile(), this);
    }
    
    public void load() {
        if(sync) {
            Schedulers.sync().runRepeating(this, checkInterval, checkInterval).bindWith(this);
        } else {
            Schedulers.async().runRepeating(this, checkInterval, checkInterval).bindWith(this);
        }
    }

    @Override
    public Icon<AchievementProfile> getIcon() {
        return null;
    }

    @Override
    public void run() {
        for(var profile : this.getAchievement().getSubscribedPlayers()) {
            var player = profile.getPlayer();
            
            if(player != null) {
                if(checkConditions(player,player)) {
                    incrementStat(player);
                }
            }
        }
    }

}
