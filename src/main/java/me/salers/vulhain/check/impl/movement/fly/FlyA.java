package me.salers.vulhain.check.impl.movement.fly;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.MovementProcessor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;


public class FlyA extends Check {

    public FlyA(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if (packet instanceof PacketPlayInFlying) {

            final MovementProcessor movementProcessor = playerData.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final double airDrag = 0.98F;
            final double gravity = 0.08F;

            final double prediction = (lastDeltaY * airDrag) - gravity;
            final double fixedPrediction = Math.abs(prediction) < 0.005 ? 0 : Math.abs(prediction);

            final double difference = Math.abs(deltaY - fixedPrediction);

            final boolean exempt = playerData.getBukkitPlayerFromUUID().isOnGround() ||
                    movementProcessor.getAirTicks() < 11 ||
                    movementProcessor.isInLiquid() ||
                    movementProcessor.isOnClimbable() ||
                    movementProcessor.isInWeb() ||
                    playerData.getBukkitPlayerFromUUID().getFallDistance() > 8.0D;


            if (difference > 0.01 && !exempt) {
                if (++buffer > getMaxBuffer()) {
                    flag(playerData, "diff=" + difference);
                }
            } else if (buffer > 0) buffer -= 0.05D;
        }
    }
}
