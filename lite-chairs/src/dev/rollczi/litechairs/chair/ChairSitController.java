/*
 * Copyright (c) 2022 Rollczi
 */

package dev.rollczi.litechairs.chair;

import dev.rollczi.litechairs.math.PositionConverter;
import dev.rollczi.litechairs.nms.api.Chair;
import dev.rollczi.litechairs.nms.api.NmsAccessor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class ChairSitController implements Listener {

    private final ChairService chairService;
    private final NmsAccessor nmsAccessor;

    public ChairSitController(ChairService chairService, NmsAccessor nmsAccessor) {
        this.chairService = chairService;
        this.nmsAccessor = nmsAccessor;
    }

    @EventHandler
    public void onSit(PlayerInteractEvent interactEvent) {
        Player player = interactEvent.getPlayer();

        if (!interactEvent.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Block clickedBlock = interactEvent.getClickedBlock();

        if (clickedBlock == null) {
            return;
        }

        if (!(clickedBlock.getBlockData() instanceof Stairs stairs)) {
            return;
        }

        if (stairs.getHalf() == Bisected.Half.TOP) {
            return;
        }

        if (player.isSneaking()) {
            return;
        }

        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            return;
        }

        interactEvent.setCancelled(true);

        Location location = clickedBlock.getLocation().add(0.5, -1.2, 0.5);

        if (player.getLocation().distance(clickedBlock.getLocation().add(0.5, 0, 0.5)) >= 2.5) {
            return;
        }

        if (chairService.isChair(PositionConverter.convert(clickedBlock.getLocation()))) {
            return;
        }

        if (chairService.isSit(player)) {
            return;
        }

        Location playerLocation = player.getLocation();
        Location playerSitLocation = player.getLocation().clone();

        playerSitLocation.setYaw(stairs.getFacing().getModY() == 0 ? playerLocation.getYaw() : playerLocation.getYaw() + 180);
        playerSitLocation.setPitch(0.0F);

        player.teleport(playerSitLocation);

        Chair chair = nmsAccessor.spawn(location, player, this.chairService::standUp);
        SittingPlayer sittingPlayer = new SittingPlayer(chair, player.getUniqueId(), PositionConverter.convert(playerLocation));

        this.chairService.sitDown(sittingPlayer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Optional<SittingPlayer> sittingPlayer = chairService.getSittingPlayer(player.getUniqueId());

        if (sittingPlayer.isEmpty()) {
            return;
        }

        chairService.standUp(sittingPlayer.get());
    }

}
