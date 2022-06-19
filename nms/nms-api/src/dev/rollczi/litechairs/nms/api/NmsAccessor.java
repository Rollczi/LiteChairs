package dev.rollczi.litechairs.nms.api;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface NmsAccessor {

    Chair spawn(Location location, Player player, Consumer<Integer> deathCallBack);

    boolean isChair(ArmorStand armorStand);

}
