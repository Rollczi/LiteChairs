/*
 * Copyright (c) 2022 Rollczi
 */

package dev.rollczi.litechairs.nms.v1_19R1;

import com.google.common.collect.ImmutableList;
import dev.rollczi.litechairs.nms.api.Chair;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        if (this.passengers().isEmpty() || !(this.passengers().get(0) instanceof EntityPlayer)) {
            this.kill();
            return;
        }

        Entity rider = this.passengers().get(0);

        if (rider instanceof EntityPlayer) {
            float yaw = rider.dr();               // float yaw = rider.getYaw();
            this.w = yaw;                         // this.prevYaw = yaw;
            this.o(yaw);                          // this.setYaw(yaw);

            this.p(rider.dt() * 0.5F);         // this.setYaw(rider.getPitch() * 0.5F);
            this.a(this.dr(), this.dt());         // this.setRot(this.getYaw(), this.getPitch());
            this.bh = this.dr();                  // this.run = this.getYaw();
        }
    }

    @Override
    public void kill() {
        this.be = true; // this.dead = true;
        this.deathCallBack.accept(this.getId());
    }

    private ImmutableList<Entity> passengers() {
        return ((Entity) this).au;
    }

    @Override
    public int getId() {
        return this.ae();
    }

    static Chair spawn(Location location, Player player, Consumer<Integer> deathCallBack) {
        org.bukkit.World world = location.getWorld();

        if (!(world instanceof CraftWorld craftWorld)) {
            throw new IllegalStateException();
        }

        World mcWorld = craftWorld.getHandle();
        Chair1_19R1 customEntity = new Chair1_19R1(mcWorld, location.getX(), location.getY(), location.getZ(), deathCallBack);
        customEntity.e(true);           // customEntity.setNoGravity(true)

        if (!(customEntity.getBukkitEntity() instanceof ArmorStand armorStand)) {
            throw new IllegalStateException();
        }

        armorStand.setRemoveWhenFarAway(false);

        mcWorld.addFreshEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        NBTTagCompound nbt = new NBTTagCompound();

        customEntity.e(nbt);                  // customEntity.save(nbt);
        nbt.a("Invulnerable", true);          // nbt.setBoolean("Invulnerable", true);
        customEntity.g(nbt);                  // customEntity.load(nbt)
        customEntity.b(nbt);                  // customEntity.saveData(nbt)
        nbt.a("Invisible", true);             // nbt.setBoolean("Invisible", true);
        nbt.a("DisabledSlots", 2031616);      // nbt.setInt("DisabledSlots", 2031616);
        //customEntity.a(nbt);                  // customEntity.readAdditionalSaveData(nbt)

        // Invoke customEntity.a(nbt) method (IDE is not cooperating)
        try {
            Method method = customEntity.getClass().getMethod("a", NBTTagCompound.class);
            method.invoke(customEntity, nbt);
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        customEntity.getBukkitEntity().setPassenger(player);

        return customEntity;
    }

}

