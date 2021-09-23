package me.salers.vulhain.processor;

import io.github.retrooper.packetevents.packetwrappers.play.in.entityaction.WrappedPacketInEntityAction;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.salers.vulhain.data.PlayerData;

@Getter
@RequiredArgsConstructor
public class ActionProcessor {

    private final PlayerData data;

    private boolean isSprinting;

    public void handleAction(final WrappedPacketInEntityAction wrapper) {
        switch (wrapper.getAction()) {
            case START_SPRINTING:
                this.isSprinting = true;
                break;
            case STOP_SPRINTING:
                this.isSprinting = false;
        }
    }
}
