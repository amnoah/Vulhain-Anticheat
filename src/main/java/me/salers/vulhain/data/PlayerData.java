package me.salers.vulhain.data;


import lombok.Data;
import me.salers.vulhain.check.Check;
import me.salers.vulhain.manager.CheckManager;
import me.salers.vulhain.processor.ActionProcessor;
import me.salers.vulhain.processor.CombatProcessor;
import me.salers.vulhain.processor.MovementProcessor;
import me.salers.vulhain.processor.RotationProcessor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Data
public class PlayerData {

    private final MovementProcessor movementProcessor;
    private final CombatProcessor combatProcessor;
    private final RotationProcessor rotationProcessor;
    private final ActionProcessor actionProcessor;

    private final CheckManager checkManager;


    private UUID uuid;


    public PlayerData(UUID uuid) {
        this.movementProcessor = new MovementProcessor(this);
        this.combatProcessor = new CombatProcessor(this);
        this.actionProcessor = new ActionProcessor(this);
        this.rotationProcessor = new RotationProcessor(this);
        this.checkManager = new CheckManager(this);
        this.uuid = uuid;

    }

    /**
     * Getting a bukkit player from a uuid
     *
     * @return the player reliated to the uuid
     */
    public Player getBukkitPlayerFromUUID() {
        return Bukkit.getPlayer(uuid);
    }
}

