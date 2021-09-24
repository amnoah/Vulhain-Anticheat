package me.salers.vulhain.processor;


import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import lombok.Data;
import me.salers.vulhain.data.PlayerData;

@Data
public class RotationProcessor {


    private float yaw, pitch,
            deltaYaw, deltaPitch,
            lastYaw, lastPitch,
            lastDeltaYaw, lastDeltaPitch;
    private PlayerData data;


    public RotationProcessor(PlayerData data) {
        this.data = data;
    }

    public void handleRotation(WrappedPacketInFlying wrapper) {
        this.yaw = wrapper.getYaw();
        this.pitch = wrapper.getPitch();

        deltaYaw = Math.abs(yaw - lastYaw);
        deltaPitch = Math.abs(pitch - lastPitch);

        lastYaw = yaw;
        lastPitch = pitch;

        lastDeltaYaw = deltaYaw;
        lastDeltaPitch = deltaPitch;
    }
}
