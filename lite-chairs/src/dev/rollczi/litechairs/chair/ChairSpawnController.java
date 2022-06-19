/*
 * Copyright (c) 2022 Rollczi
 */

package dev.rollczi.litechairs.chair;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class ChairSpawnController implements Listener {

    private final ChairService chairService;

    public ChairSpawnController(ChairService chairService) {
        this.chairService = chairService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof ArmorStand armorStand)) {
            return;
        }

        if (chairService.isChair(armorStand)) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSpawn2(CreatureSpawnEvent event) {
        if (!(event.getEntity() instanceof ArmorStand armorStand)) {
            return;
        }

        if (chairService.isChair(armorStand)) {
            event.setCancelled(false);
        }
    }

}
