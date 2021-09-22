package me.salers.vulhain.processor;


import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import me.salers.vulhain.data.PlayerData;
import lombok.Data;

@Data
public class RotationProcessor {


    private float yaw, pitch,
            deltaYaw, deltaPitch,
            lastYaw, lastPitch,
            lastDeltaYaw, lastDeltaPitch;
    private PlayerData data;


    public RotationProcessor(PlayerData data) {
       this.data =data;
    }

    public void handleRotation(WrappedPacketInFlying wrapper) {
        /* Getting yaw one tick ago */
        lastYaw = yaw;
        yaw = wrapper.getYaw() % 360;
        deltaYaw = Math.abs(yaw - lastYaw) % 360;

        /* Getting pitch one tick ago */
        lastPitch = pitch;
        pitch = wrapper.getPitch();
        deltaPitch = Math.abs(pitch - lastPitch);

    }
}
