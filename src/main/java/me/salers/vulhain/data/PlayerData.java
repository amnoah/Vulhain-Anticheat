package me.salers.vulhain.data;


import me.salers.vulhain.check.Check;
import me.salers.vulhain.check.CheckManager;
import me.salers.vulhain.processor.CombatProcessor;
import me.salers.vulhain.processor.MovementProcessor;
import me.salers.vulhain.processor.RotationProcessor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Data
public class PlayerData {

    private MovementProcessor movementProcessor;
    private CombatProcessor combatProcessor;

    private RotationProcessor rotationProcessor;
    private CheckManager checkManager;



    private UUID uuid;
    private List<Check> checks;

    public PlayerData(UUID uuid) {
        this.movementProcessor = new MovementProcessor(this);
        this.combatProcessor = new CombatProcessor(this);

        this.rotationProcessor = new RotationProcessor(this);
        this.checkManager = new CheckManager(this);
        this.uuid = uuid;
        this.checks = this.checkManager.getChecks();
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
