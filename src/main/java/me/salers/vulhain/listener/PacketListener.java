package me.salers.vulhain.listener;


import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.packettype.PacketType;
import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import me.salers.vulhain.Vulhain;
import me.salers.vulhain.check.Check;
import me.salers.vulhain.data.PlayerData;

public class PacketListener extends PacketListenerDynamic {


    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        final PlayerData data = Vulhain.getInstance().getPlayerDataManager().getPlayerData(event.getPlayer().getUniqueId());

        if (data == null) return;

        if (PacketType.Play.Client.Util.isInstanceOfFlying(event.getPacketId())) {
            final WrappedPacketInFlying wrapped = new WrappedPacketInFlying(event.getNMSPacket());
            data.getRotationProcessor().handleRotation(wrapped);
            data.getMovementProcessor().handleMove(wrapped);
        } else if (event.getPacketId() == PacketType.Play.Client.USE_ENTITY) {
            final WrappedPacketInUseEntity wrappedPacketInUseEntity = new WrappedPacketInUseEntity(event.getNMSPacket());
            data.getCombatProcessor().handleCombat(wrappedPacketInUseEntity);
        } else if(event.getPacketId() == PacketType.Play.Client.ENTITY_ACTION) {
            final WrappedPacketInEntityAction wrapper = new WrappedPacketInEntityAction(event.getNMSPacket());
            data.getActionProcessor().handleAction(wrapper);
        }

        for (Check checks : data.getCheckManager().getChecks()) {
            if (checks.isEnabled()) {
                checks.onPacket(event.getNMSPacket().getRawNMSPacket(), data);
            }
        }

    }
}
