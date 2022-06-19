package dev.rollczi.litechairs.chair;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Collection;

public class ChairTeleportController implements Listener {

    private final ChairService chairService;

    public ChairTeleportController(ChairService chairService) {
        this.chairService = chairService;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Location location = event.getTo();

        if (location == null || location.getWorld() == null) {
            return;
        }

        Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, 0.1, 0.1, 0.1);

        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof Player targetPlayer)) {
                continue;
            }

            if (chairService.isSit(targetPlayer)) {
                event.setTo(location.add(0, 1, 0));
                break;
            }
        }

        if (!chairService.isSit(event.getPlayer())) {
            return;
        }

        if (event.getPlayer().getVehicle() instanceof ArmorStand armorStand) {
            if (chairService.isChair(armorStand)) {
                chairService.standUp(armorStand.getEntityId());
            }
        }
    }

}
