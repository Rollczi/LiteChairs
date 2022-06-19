package dev.rollczi.litechairs.math;

import java.util.Objects;

public class Position {

    private final double x;
    private final double y;
    private final double z;
    private final String world;

    public Position(double x, double y, double z, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return Double.compare(position.x, x) == 0 && Double.compare(position.y, y) == 0 && Double.compare(position.z, z) == 0 && world.equals(position.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, world);
    }

}
