package me.salers.vulhain.check.impl.movement.step;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.MovementProcessor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class StepA extends Check {

    private int groundTicks;

    public StepA(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if(packet instanceof PacketPlayInFlying) {

            final MovementProcessor movementProcessor = playerData.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();

            if(playerData.getBukkitPlayerFromUUID().isOnGround())
                groundTicks++;
            else groundTicks = 0;

            if(groundTicks >= 2 && deltaY > 0.6D) {
                if (++buffer > getMaxBuffer()) {
                    flag(playerData, "delta=" + deltaY);
                }
            } else if (buffer > 0) buffer -= 0.05D;
        }
    }
}
