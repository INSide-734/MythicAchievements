package io.lumine.achievements.compat;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

import io.lumine.achievements.MythicAchievementsPlugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardSupport {

    public static StateFlag SPRAY_FLAG;

    public WorldGuardSupport(MythicAchievementsPlugin plugin) {

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try{
            StateFlag flag = new StateFlag("mcc-spray-flag", true);
            registry.register(flag);
            SPRAY_FLAG = flag;
        } catch(FlagConflictException e){
            Flag<?> existing = registry.get("mcc-spray-flag");
            if(existing instanceof StateFlag){
                SPRAY_FLAG = (StateFlag) existing;
            }
        }

    }

    public boolean canSprayWG(Player player, Location location) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(location));

        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);

        return set.testState(localPlayer, SPRAY_FLAG);
    }
}
