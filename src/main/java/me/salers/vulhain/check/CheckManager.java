package me.salers.vulhain.check;


import me.salers.vulhain.check.impl.movement.FlyA;
import me.salers.vulhain.check.impl.movement.FlyB;
import me.salers.vulhain.check.impl.movement.FlyC;
import me.salers.vulhain.data.PlayerData;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class CheckManager {

    private final PlayerData data;

    private final List<Check> checks;

    public CheckManager(PlayerData data) {
        this.data = data;
        this.checks = Arrays.asList(

                new FlyA("Fly","movement","A",true),
                new FlyB("Fly","movement","B",false),
                new FlyC("Fly","movement","C",false)




        );
    }
}
