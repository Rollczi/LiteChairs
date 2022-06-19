/*
 * Copyright (c) 2022 Rollczi
 */

package dev.rollczi.litechairs.nms.v1_19R1;

import dev.rollczi.litechairs.nms.api.Chair;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;
import java.util.function.Consumer;

// IDE is not cooperating :(
// 'x()' in 'net.minecraft.world.entity.decoration.EntityArmorStand' clashes with 'x()' in 'net.minecraft.world.entity.EntityLiving'; attempting to use incompatible return type
// but java compile this

public class Chair1_19R1 extends EntityArmorStand implements Chair {

    private final Consumer<Integer> deathCallBack;

    private Chair1_19R1(World world, double x, double y, double z, Consumer<Integer> deathCallBack) {
        super(world, x, y, z);
        this.deathCallBack = deathCallBack;
    }

    @Override
    public void k() {
        if (this.passengers().isEmpty() || !(this.passengers().get(0) instanceof Player rider)) {
            this.kill();
            return;
        }

        float pitch = rider.getEyeLocation().getPitch();;
        float yaw = rider.getEyeLocation().getYaw();

        this.getBukkitEntity().setRotation(yaw, pitch * 0.5F);
    }

    @Override
    public void kill() {
        this.getBukkitEntity().remove();
        this.deathCallBack.accept(this.getId());
    }

    @Override
    public int getId() {
        return this.getBukkitEntity().getEntityId();
    }

    private List<org.bukkit.entity.Entity> passengers() {
        return this.getBukkitEntity().getPassengers();
    }

    static Chair spawn(Location location, Player player, Consumer<Integer> deathCallBack) {
        org.bukkit.World world = location.getWorld();

        if (!(world instanceof CraftWorld craftWorld)) {
            throw new IllegalStateException();
        }

        World mcWorld = craftWorld.getHandle();
        Chair1_19R1 customEntity = new Chair1_19R1(mcWorld, location.getX(), location.getY(), location.getZ(), deathCallBack);

        if (!(customEntity.getBukkitEntity() instanceof ArmorStand armorStand)) {
            throw new IllegalStateException();
        }

        armorStand.setGravity(false);
        armorStand.setRemoveWhenFarAway(false);

        mcWorld.addFreshEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);

        armorStand.addEquipmentLock(EquipmentSlot.HAND, LockType.ADDING_OR_CHANGING);
        armorStand.addEquipmentLock(EquipmentSlot.OFF_HAND, LockType.ADDING_OR_CHANGING);

        armorStand.addPassenger(player);

        return customEntity;
    }

}

