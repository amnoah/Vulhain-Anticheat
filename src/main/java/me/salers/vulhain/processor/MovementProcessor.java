package me.salers.vulhain.processor;


import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;
import lombok.Data;
import me.salers.vulhain.data.PlayerData;
import me.salers.vulhain.utils.LocationUtils;

@Data
public class MovementProcessor {

    private double x, y, z, lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ, deltaXZ, lastDeltaX, lastDeltaY, lastDeltaZ, lastDeltaXZ;
    private PlayerData data;
    private int airTicks, edgeBlockTicks;
    private boolean isNearBoat, isInLiquid, isInWeb, isOnClimbable, isAtTheEdgeOfABlock,ground;

    public MovementProcessor(PlayerData data) {
        this.data = data;

    }

    public void handleMove(WrappedPacketInFlying wrapper) {
        if (wrapper.isPosition()) {
            //from Medusa START
            lastX = this.x;
            lastY = this.y;
            lastZ = this.z;

            this.x = wrapper.getX();
            this.y = wrapper.getY();
            this.z = wrapper.getZ();

            lastDeltaX = deltaX;
            lastDeltaY = deltaY;
            lastDeltaZ = deltaZ;
            lastDeltaXZ = deltaXZ;

            deltaX = this.x - lastX;
            deltaY = this.y - lastY;
            deltaZ = this.z - lastZ;
            deltaXZ = Math.hypot(deltaX, deltaZ);

            //END


            /** Getting since how many ticks player is in air **/

            if (LocationUtils.isCloseToGround(data.getBukkitPlayerFromUUID().getLocation())) {
                airTicks = 0;
            } else airTicks++;

            if (LocationUtils.isAtEdgeOfABlock(data.getBukkitPlayerFromUUID())) {
                edgeBlockTicks++;
            } else edgeBlockTicks = 0;


            isNearBoat = LocationUtils.isNearBoat(data.getBukkitPlayerFromUUID());
            isInLiquid = LocationUtils.isInLiquid(data.getBukkitPlayerFromUUID());
            isInWeb = LocationUtils.isCollidingWithWeb(data.getBukkitPlayerFromUUID());
            isOnClimbable = LocationUtils.isCollidingWithClimbable(data.getBukkitPlayerFromUUID());
            ground = wrapper.isOnGround();


        }


    }
}
