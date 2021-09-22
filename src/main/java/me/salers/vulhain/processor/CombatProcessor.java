package me.salers.vulhain.processor;


import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import lombok.Data;
import me.salers.vulhain.data.PlayerData;
import org.bukkit.entity.LivingEntity;

@Data
public class CombatProcessor {

    private LivingEntity attacked, lastAttacked;

    private long lastAttack;

    private PlayerData data;


    public CombatProcessor(PlayerData data) {
        this.data = data;
    }

    public void handleCombat(WrappedPacketInUseEntity wrapper) {
        if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {

            attacked = (LivingEntity) wrapper.getEntity();
            lastAttack = System.currentTimeMillis();

            setLastAttacked(attacked);
        }
    }
}
