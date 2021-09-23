package me.salers.vulhain.check.impl.movement.speed;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.MovementProcessor;
import me.salers.vulhain.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.NumberConversions;

public class SpeedA extends Check {

    /**
     * this check was made by Orress (github.com/Orress) originally for HBAC (my old ac) so im taking it & giving credit.
     */

    private int air,ground;
    private float friction,lastFriction;

    public SpeedA(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if(packet instanceof PacketPlayInFlying) {
            final MovementProcessor movementProcessor = playerData.getMovementProcessor();


            boolean onGround = movementProcessor.isGround();


            air = onGround ? 0 : Math.min(air + 1, 20);
            ground = onGround ? Math.min(ground + 1, 20) : 0;

            // deltas
            double deltaXZ = movementProcessor.getDeltaXZ();
            double lastDeltaXZ = movementProcessor.getLastDeltaXZ();

            // landMovementFactor
            float speed = PlayerUtils.getPotionLevel(playerData.getBukkitPlayerFromUUID(), PotionEffectType.SPEED);
            float slow = PlayerUtils.getPotionLevel(playerData.getBukkitPlayerFromUUID(), PotionEffectType.SLOW);
            double d = 0.10000000149011612;
            d += d * 0.20000000298023224 * speed;
            d += d * -0.15000000596046448 * slow;

            // Sprint desync big gay just assume they are sprinting
            d += d * 0.30000001192092896;

            float landMovementFactor = (float) d;

            // the check itself
            double prediction;
            if (ground > 2) {
                prediction = lastDeltaXZ * 0.91f * getBlockFriction(playerData) + landMovementFactor;
            } else if (air == 1) {
                prediction = lastDeltaXZ * 0.91f + 0.2f + landMovementFactor;
            } else if (ground == 2) {
                prediction = lastDeltaXZ * 0.91f + landMovementFactor;
            } else {
                prediction = lastDeltaXZ * 0.91f + 0.026f;
            }
            if (prediction < playerData.getBukkitPlayerFromUUID().getWalkSpeed() + 0.02 * (speed + 1))
                prediction = playerData.getBukkitPlayerFromUUID().getWalkSpeed() + 0.02 * (speed + 1);

            // very lazy patch for a false flag
            if (ground > 1) {
                this.lastFriction = this.friction;
                this.friction = getBlockFriction(playerData) * 0.91f;
            }

            if (friction < lastFriction)
                prediction += landMovementFactor * 1.25;




            // flag
            if (deltaXZ > prediction) {
                if (++buffer > getMaxBuffer()) {
                    flag(playerData, "p=" + prediction + " d=" + deltaXZ);
                }
            } else if(buffer > 0) buffer -= 0.5D;
        }
    }

    public float getBlockFriction(PlayerData playerData) {
        String block = playerData.getBukkitPlayerFromUUID().getLocation().add(0, -1, 0).getBlock().getType().name().toLowerCase();
        return block.equals("blue ice") ? 0.989f : block.contains("ice") ? 0.98f : block.equals("slime") ? 0.8f : 0.6f;
    }


}
