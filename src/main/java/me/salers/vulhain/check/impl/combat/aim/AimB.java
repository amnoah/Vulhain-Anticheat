package me.salers.vulhain.check.impl.combat.aim;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.RotationProcessor;

public class AimB extends Check {


    public AimB(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {

        final RotationProcessor rotationProcessor = playerData.getRotationProcessor();

        final double deltaYaw = rotationProcessor.getDeltaYaw();
        final double deltaPitch = rotationProcessor.getDeltaPitch();

        if(Double.toString(deltaPitch).contains("E") || Double.toString(deltaYaw).contains("E")) {
            if(++buffer > getMaxBuffer()) {
                flag(playerData,"too small delta");
            }
        } else if(buffer > 0) buffer -= 0.5D;


    }
}
