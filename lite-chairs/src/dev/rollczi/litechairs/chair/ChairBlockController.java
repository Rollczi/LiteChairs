/*
 * Copyright (c) 2022 Rollczi
 */

package dev.rollczi.litechairs.chair;

import dev.rollczi.litechairs.math.Position;
import dev.rollczi.litechairs.math.PositionConverter;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class ChairBlockController implements Listener {

    private final ChairService service;

    public ChairBlockController(ChairService service) {
        this.service = service;
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        if (!service.isChair(PositionConverter.convert(event.getBlock().getLocation()))) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void pistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            Position position = PositionConverter.convert(block.getLocation());

            if (!service.isChair(position)) {
                continue;
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void pistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            Position position = PositionConverter.convert(block.getLocation());

            if (!service.isChair(position)) {
                continue;
            }

            event.setCancelled(true);
            return;
        }
    }

}
