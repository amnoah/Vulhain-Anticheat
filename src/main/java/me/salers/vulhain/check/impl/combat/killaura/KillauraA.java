package me.salers.vulhain.check.impl.combat.killaura;

import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;

public class KillauraA extends Check {

    private long lastSent;

    public KillauraA(String name, String category, String type, boolean experimental) {
        super(name, category, type, experimental);
    }

    @Override
    public void onPacket(Object packet, PlayerData playerData) {
        if (packet instanceof PacketPlayInFlying) {
            lastSent = System.currentTimeMillis();
        } else if (packet instanceof PacketPlayInUseEntity) {

            final long elapsed = System.currentTimeMillis() - lastSent;

            if (elapsed < 25L) {
                if (++buffer > getMaxBuffer()) {
                    flag(playerData, "delta=" + elapsed);
                }
            } else if (buffer > 0) buffer -= 0.05D;
        }
    }
}

