package me.salers.vulhain.check.impl.combat.aim;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.RotationProcessor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class AimA extends Check {

    public AimA(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if(packet instanceof PacketPlayInFlying) {

            final RotationProcessor rotationProcessor = playerData.getRotationProcessor();

            final double deltaYaw = rotationProcessor.getDeltaYaw();
            final double lastDeltaYaw = rotationProcessor.getLastDeltaYaw();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double lastDeltaPitch = rotationProcessor.getLastDeltaPitch();

            final double accelYaw = Math.abs(deltaYaw - lastDeltaYaw);
            final double accelPitch = Math.abs(deltaPitch - lastDeltaPitch);

            final boolean exempt = deltaYaw < 5.0D;

            if((accelPitch <= 0.001D || accelYaw <= 0.001D) && !exempt) {
                if(++buffer > getMaxBuffer()) {
                    flag(playerData,"too small accel");
                }
            } else if(buffer > 0) buffer -= 0.5D;
        }
    }
}
