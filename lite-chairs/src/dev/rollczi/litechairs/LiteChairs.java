package dev.rollczi.litechairs;

import dev.rollczi.litechairs.nms.api.NmsAccessor;
import dev.rollczi.litechairs.nms.v1_19R1.NmsAccessor1_19R1;
import dev.rollczi.litechairs.chair.ChairBlockController;
import dev.rollczi.litechairs.chair.ChairSitController;
import dev.rollczi.litechairs.chair.ChairSpawnController;
import dev.rollczi.litechairs.chair.ChairTeleportController;
import dev.rollczi.litechairs.chair.SittingPlayer;
import dev.rollczi.litechairs.chair.ChairService;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class LiteChairs extends JavaPlugin {

    private ChairService chairService;

    @Override
    public void onEnable() {
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        NmsAccessor nmsAccessor = new NmsAccessor1_19R1();

        this.chairService = new ChairService(server, nmsAccessor);

        pluginManager.registerEvents(new ChairSitController(this.chairService, nmsAccessor), this);
        pluginManager.registerEvents(new ChairTeleportController(this.chairService), this);
        pluginManager.registerEvents(new ChairBlockController(this.chairService), this);
        pluginManager.registerEvents(new ChairSpawnController(this.chairService), this);
    }

    @Override
    public void onDisable() {
        for (SittingPlayer sittingPlayer : chairService.getSittingPlayers()) {
            this.chairService.standUp(sittingPlayer);
        }
    }

}
