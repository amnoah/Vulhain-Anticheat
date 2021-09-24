package me.salers.vulhain.check.impl.movement.nofall;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.MovementProcessor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;

public class NoFallA extends Check {

    public NoFallA(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if(packet instanceof PacketPlayInFlying) {

            final MovementProcessor movementProcessor = playerData.getMovementProcessor();

            final boolean packetGround = movementProcessor.isGround(); //this can be spoofed
            final boolean serverGround = movementProcessor.getAirTicks() < 2; //cannot be spoofed (as i know..)

            final boolean exempt = playerData.getBukkitPlayerFromUUID().isInsideVehicle() ||
                    movementProcessor.isAtTheEdgeOfABlock() || movementProcessor.isOnClimbable();

            if(!exempt && packetGround && !serverGround) {
                if(++buffer > getMaxBuffer()) {
                    flag(playerData,"aT=" + movementProcessor.getAirTicks());
                }
            } else if(buffer > 0) buffer -= 0.05D;
        }
    }
}
