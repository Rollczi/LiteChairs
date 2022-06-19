/*
 * Copyright (c) 2022 Rollczi
 */

package dev.rollczi.litechairs.nms.v1_19R1;

import dev.rollczi.litechairs.nms.api.Chair;
import dev.rollczi.litechairs.nms.api.NmsAccessor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class NmsAccessor1_19R1 implements NmsAccessor {

    @Override
    public Chair spawn(Location location, Player player, Consumer<Integer> deathCallBack) {
        return Chair1_19R1.spawn(location, player, deathCallBack);
    }

    @Override
    public boolean isChair(ArmorStand armorStand) {
        if (!(armorStand instanceof CraftArmorStand craftArmorStand)) {
            return false;
        }

        return craftArmorStand.getHandle() instanceof Chair1_19R1;
    }

}
