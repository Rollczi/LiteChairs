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
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Optional;

public class ChairSitController implements Listener {

    private static final Map<Integer, Float> STAIRS_TO_YAW = Map.of(
            0, 90F,
            1, - 90F,
            2, 180F,
            3, 0F
    );

    private final ChairService chairService;
    private final NmsAccessor nmsAccessor;

    public ChairSitController(ChairService chairService, NmsAccessor nmsAccessor) {
        this.chairService = chairService;
        this.nmsAccessor = nmsAccessor;
    }

    @EventHandler
    public void spawnStairs(PlayerInteractEvent interactEvent) {
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

        if (stairs.getFacing() == BlockFace.UP) {
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

        ItemStack asItem = clickedBlock.getState().getData().toItemStack(1);
        ItemMeta itemMeta = asItem.getItemMeta();

        if (!(itemMeta instanceof Damageable damageable)) {
            return;
        }

        int damage = damageable.getDamage();

        Location playerLocation = player.getLocation();
        Location toEdit = player.getLocation().clone();

        if (!STAIRS_TO_YAW.containsKey(damage)) {
            return;
        }

        toEdit.setYaw(STAIRS_TO_YAW.get(damage));
        toEdit.setPitch(0.0F);

        player.teleport(toEdit);

        Chair chair = nmsAccessor.spawn(location, player, this.chairService::standUp);
        SittingPlayer sittingPlayer = new SittingPlayer(chair, player.getUniqueId(), PositionConverter.convert(playerLocation));

        this.chairService.sitDown(sittingPlayer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Optional<SittingPlayer> sittingPlayer = chairService.getSittingPlayer(player.getUniqueId());

        if (sittingPlayer.isEmpty()) {
            return;
        }

        chairService.standUp(sittingPlayer.get());
    }

}
