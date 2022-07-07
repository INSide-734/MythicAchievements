package io.lumine.achievements.nms;

import lombok.Getter;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import io.lumine.achievements.MythicAchievementsPlugin;

public class VolatileCodeEnabled_v1_19_R1 implements VolatileCodeHandler {

    @Getter private final MythicAchievementsPlugin plugin;
    
    public VolatileCodeEnabled_v1_19_R1(MythicAchievementsPlugin plugin) {
        this.plugin = plugin;
    }

    public void broadcast(Packet<?>... packets) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            var connection = ((CraftPlayer) player).getHandle().connection;
            for(Packet<?> packet : packets) {
                connection.send(packet);
            }
        }
    }

    public void broadcast(Player player, Packet<?>... packets) {
        var connection = ((CraftPlayer) player).getHandle().connection;
        for(Packet<?> packet : packets) {
            connection.send(packet);
        }
    }

}
