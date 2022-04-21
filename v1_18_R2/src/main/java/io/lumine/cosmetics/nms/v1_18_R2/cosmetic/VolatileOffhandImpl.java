package io.lumine.cosmetics.nms.v1_18_R2.cosmetic;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import io.lumine.cosmetics.MCCosmeticsPlugin;
import io.lumine.cosmetics.api.cosmetics.Cosmetic;
import io.lumine.cosmetics.api.cosmetics.ItemCosmetic;
import io.lumine.cosmetics.api.players.CosmeticProfile;
import io.lumine.cosmetics.managers.offhand.Offhand;
import io.lumine.cosmetics.nms.VolatileCodeEnabled_v1_18_R2;
import io.lumine.cosmetics.nms.cosmetic.VolatileEquipmentHelper;
import io.lumine.cosmetics.players.Profile;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class VolatileOffhandImpl implements VolatileEquipmentHelper {

    @Getter private final MCCosmeticsPlugin plugin;
    private final VolatileCodeEnabled_v1_18_R2 nmsHandler;
    private final Map<Integer, Player> playerTracker = Maps.newConcurrentMap();

    public VolatileOffhandImpl(MCCosmeticsPlugin plugin, VolatileCodeEnabled_v1_18_R2 nmsHandler) {
        this.plugin = plugin;
        this.nmsHandler = nmsHandler;
    }
    
    @Override
    public void apply(CosmeticProfile profile) {

        if (profile == null)
            return;

        Player player = profile.getPlayer();
        Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquipped(Offhand.class);

        if (cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic offhand))
            return;

        var nmsOffhand = CraftItemStack.asNMSCopy(offhand.getCosmetic());

        playerTracker.put(player.getEntityId(), player);

        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), List.of(Pair.of(EquipmentSlot.OFFHAND, nmsOffhand)));

        nmsHandler.broadcastAround(player, equipmentPacket);

    }

    @Override
    public List<Object> write(Player receiver, Object packet) {
        if(packet instanceof ClientboundAddPlayerPacket playerPacket) {
            int id = playerPacket.getEntityId();
            Profile profile = getProfile(receiver, id);
            if(profile != null)
                return handleSpawn(profile);
        }else if(packet instanceof ClientboundSetEquipmentPacket equipmentPacket) {
            int id = equipmentPacket.getEntity();
            Profile profile = getProfile(receiver, id);
            if(profile != null)
                return handleSpawn(profile);
        }

        return null;
    }

    private Profile getProfile(Player receiver, int id) {
        final var entity = nmsHandler.getEntity(receiver.getWorld(), id);
        if(!(entity instanceof Player player))
            return null;
        return plugin.getProfiles().getProfile(player);
    }

    public List<Object> handleSpawn(Profile profile) {
        Optional<Cosmetic> cosmetic = profile.getCosmeticInventory().getEquipped(Offhand.class);
        if(cosmetic.isEmpty() || !(cosmetic.get() instanceof ItemCosmetic offhand))
            return null;

        final var player = profile.getPlayer();
        final var nmsOffhand = CraftItemStack.asNMSCopy(offhand.getCosmetic());
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), List.of(Pair.of(EquipmentSlot.OFFHAND, nmsOffhand)));

        return List.of(equipmentPacket);
    }

}