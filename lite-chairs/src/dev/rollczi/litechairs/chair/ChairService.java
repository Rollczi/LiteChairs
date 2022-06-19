package dev.rollczi.litechairs.chair;

import dev.rollczi.litechairs.math.Position;
import dev.rollczi.litechairs.math.PositionConverter;
import dev.rollczi.litechairs.nms.api.NmsAccessor;
import org.bukkit.Server;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ChairService {

    private final Server server;
    private final NmsAccessor nmsAccessor;

    private final Map<Integer, SittingPlayer> sittingByEntityId = new HashMap<>();
    private final Map<Position, SittingPlayer> sittingByPosition = new HashMap<>();
    private final Map<UUID, SittingPlayer> sittingByPlayer = new HashMap<>();

    public ChairService(Server server, NmsAccessor nmsAccessor) {
        this.server = server;
        this.nmsAccessor = nmsAccessor;
    }

    public Optional<SittingPlayer> getSittingPlayer(Integer id) {
        return Optional.ofNullable(sittingByEntityId.get(id));
    }

    public Optional<SittingPlayer> getSittingPlayer(UUID player) {
        return Optional.ofNullable(this.sittingByPlayer.get(player));
    }

    public Collection<SittingPlayer> getSittingPlayers() {
        return Collections.unmodifiableCollection(sittingByEntityId.values());
    }

    public void sitDown(SittingPlayer sittingPlayer) {
        this.sittingByEntityId.put(sittingPlayer.getChair().getId(), sittingPlayer);
        this.sittingByPosition.put(sittingPlayer.getPosition(), sittingPlayer);
        this.sittingByPlayer.put(sittingPlayer.getPlayer(), sittingPlayer);
    }

    public void standUp(SittingPlayer sittingPlayer) {
        Player player = server.getPlayer(sittingPlayer.getPlayer());

        if (player != null) {
            player.teleport(PositionConverter.convert(sittingPlayer.getPosition()));
        }

        sittingByEntityId.remove(sittingPlayer.getChair().getId());
        sittingByPosition.remove(sittingPlayer.getPosition());
        sittingByPlayer.remove(sittingPlayer.getPlayer());
        sittingPlayer.getChair().kill();;
    }

    public void standUp(int entityId) {
        Optional<SittingPlayer> sittingPlayer = this.getSittingPlayer(entityId);

        if (sittingPlayer.isEmpty()) {
            return;
        }

        this.standUp(sittingPlayer.get());
    }

    public boolean isChair(Position position) {
        return sittingByPosition.containsKey(position);
    }

    public boolean isChair(ArmorStand armorStand) {
        return nmsAccessor.isChair(armorStand);
    }

    public boolean isSit(Player player) {
        return this.sittingByPlayer.containsKey(player.getUniqueId());
    }
}
