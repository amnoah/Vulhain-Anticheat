package me.salers.vulhain.check.impl.combat.aim;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.RotationProcessor;
import me.salers.vulhain.utils.MathUtils;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class AimC extends Check {

    /**
     * got the idea from :
     https://github.com/GladUrBad/Medusa/blob/master/Impl/src/main/java/com/gladurbad/medusa/check/impl/combat/aimassist/AimAssistE.java

     */

    public AimC(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if(packet instanceof PacketPlayInFlying) {

            final RotationProcessor rotationProcessor = playerData.getRotationProcessor();

            final double deltaPitch = rotationProcessor.getDeltaPitch();
            final double lastDeltaPitch = rotationProcessor.getLastDeltaPitch();

            final double gcd = MathUtils.gcd(0x4000,
                    (Math.abs(deltaPitch) * MathUtils.EXPANDER), (Math.abs(lastDeltaPitch) * MathUtils.EXPANDER));

            final boolean exempt = deltaPitch == 0 || lastDeltaPitch == 0;

            if(gcd < 131072L && !exempt) {
                if(++buffer > getMaxBuffer()) {
                    flag(playerData,"gcd=" + gcd);
                }

            } else if(buffer > 0) buffer -= 0.5D;
        }
    }
}
