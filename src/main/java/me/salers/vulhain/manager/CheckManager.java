package me.salers.vulhain.manager;


import lombok.Getter;
import me.salers.vulhain.check.Check;
import me.salers.vulhain.check.impl.combat.aim.AimA;
import me.salers.vulhain.check.impl.combat.aim.AimB;
import me.salers.vulhain.check.impl.combat.killaura.KillauraA;
import me.salers.vulhain.check.impl.movement.fly.FlyA;
import me.salers.vulhain.check.impl.movement.fly.FlyB;
import me.salers.vulhain.check.impl.movement.fly.FlyC;
import me.salers.vulhain.check.impl.movement.step.StepA;
import me.salers.vulhain.data.PlayerData;

import java.util.Arrays;
import java.util.List;

@Getter
public class CheckManager {

    private final PlayerData data;

    private final List<Check> checks;

    public CheckManager(PlayerData data) {
        this.data = data;
        this.checks = Arrays.asList(

                new AimA("Aim","combat","A",false),
                new AimB("Aim","combat","B",false),

                new KillauraA("KillAura","combat","A",false),

                new StepA("Step","movement","A",false),

                new FlyA("Fly", "movement", "A", true),
                new FlyB("Fly", "movement", "B", false),
                new FlyC("Fly", "movement", "C", false)


        );
    }
}
