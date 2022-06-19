package dev.rollczi.litechairs.chair;

import dev.rollczi.litechairs.math.Position;
import dev.rollczi.litechairs.nms.api.Chair;

import java.util.UUID;

public class SittingPlayer {

    private final Chair chair;
    private final UUID player;
    private final Position position;

    SittingPlayer(Chair chair, UUID player, Position position) {
        this.chair = chair;
        this.player = player;
        this.position = position;
    }

    public Chair getChair() {
        return chair;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public Position getPosition() {
        return this.position;
    }

}
