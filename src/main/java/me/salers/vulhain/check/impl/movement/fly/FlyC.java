package me.salers.vulhain.check.impl.movement.fly;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.MovementProcessor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class FlyC extends Check {

    public FlyC(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if (packet instanceof PacketPlayInFlying) {

            final MovementProcessor movementProcessor = playerData.getMovementProcessor();

            final double deltaY = movementProcessor.getDeltaY();
            final double lastDeltaY = movementProcessor.getLastDeltaY();

            final boolean exempt = playerData.getBukkitPlayerFromUUID().isOnGround() ||
                    movementProcessor.getAirTicks() < 11 ||
                    movementProcessor.isInLiquid() ||
                    movementProcessor.isOnClimbable() ||
                    movementProcessor.isInWeb() ||
                    playerData.getBukkitPlayerFromUUID().getFallDistance() > 28.0D;

            if (deltaY > lastDeltaY && !exempt) {
                if (++buffer > getMaxBuffer()) {
                    flag(playerData, "current=" + deltaY + " last=" + lastDeltaY);
                }
            } else if (buffer > 0) buffer -= 0.05D;

        }
    }
}
