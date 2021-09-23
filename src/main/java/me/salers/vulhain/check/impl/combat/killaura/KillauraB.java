package me.salers.vulhain.check.impl.combat.killaura;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.processor.MovementProcessor;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import org.bukkit.entity.EntityType;

public class KillauraB extends Check {

    private int ticks;

    public KillauraB(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if(packet instanceof PacketPlayInUseEntity) {
            if(((PacketPlayInUseEntity) packet).a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                this.ticks = 0;
            }
        } else if(packet instanceof PacketPlayInFlying) {
            final MovementProcessor movementProcessor = playerData.getMovementProcessor();

            final double deltaXZ = movementProcessor.getDeltaXZ();
            final double lastDeltaXZ = movementProcessor.getLastDeltaXZ();

            final double accelerationXZ = Math.abs(deltaXZ - lastDeltaXZ);

            if(playerData.getCombatProcessor().getAttacked() == null) return;

            if(accelerationXZ < 0.001D && deltaXZ > 0.19D &&
                    playerData.getActionProcessor().isSprinting() &&
                    playerData.getCombatProcessor().getAttacked().getType() == EntityType.PLAYER &&
                    this.ticks <= 2) {

                if(++buffer > getMaxBuffer()) {
                    buffer /= 2;
                    flag(playerData,"a=" + accelerationXZ);
                }

            } else if(buffer >= 2.5D) buffer -= 2.5D;

            this.ticks++;
        }
    }
}
