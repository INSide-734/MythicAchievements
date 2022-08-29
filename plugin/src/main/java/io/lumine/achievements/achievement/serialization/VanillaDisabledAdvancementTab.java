package io.lumine.achievements.achievement.serialization;

public class VanillaDisabledAdvancementTab {

    private final String parent = "totally_not_possible";
    
    private final VanillaDisabledAdvancementCriteria criteria = new VanillaDisabledAdvancementCriteria();
    
    private final class VanillaDisabledAdvancementCriteria {
        private final VanillaDisabledAdvancementCriteriaImpossible impossible = new VanillaDisabledAdvancementCriteriaImpossible();
    }
    
    private final class VanillaDisabledAdvancementCriteriaImpossible {
        private final String trigger = "minecraft:impossible";
    }
    
}