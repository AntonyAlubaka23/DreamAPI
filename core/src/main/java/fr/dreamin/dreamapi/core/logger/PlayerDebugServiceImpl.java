package fr.dreamin.dreamapi.core.logger;

import fr.dreamin.dreamapi.api.services.DreamAutoService;
import fr.dreamin.dreamapi.api.services.DreamService;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@DreamAutoService(PlayerDebugService.class)
public final class PlayerDebugServiceImpl implements PlayerDebugService, DreamService {

  private final @NotNull Map<UUID, Set<UUID>> debugTargets = new HashMap<>();

  // ##############################################################
  // ---------------------- SERVICE METHODS -----------------------
  // ##############################################################

  @Override
  public void startDebug(@NotNull Player target, @NotNull Player executor) {
    this.debugTargets.computeIfAbsent(target.getUniqueId(), key -> {
      final var rs = new HashSet<UUID>();
      rs.add(executor.getUniqueId());
      return rs;
    });
  }

  @Override
  public void stopDebug(@NotNull Player target, @NotNull Player executor) {
    this.debugTargets.computeIfPresent(target.getUniqueId(), (k, v) -> {
      v.remove(executor.getUniqueId());
      return v.isEmpty() ? null : v;
    });
  }

  @Override
  public boolean isDebugging(@NotNull Player target, @NotNull Player executor) {
    final var debuggers = this.debugTargets.get(target.getUniqueId());
    return debuggers != null && debuggers.contains(executor.getUniqueId());
  }

  @Override
  public Audience getDebuggers(@NotNull Player target) {
    final var ids = debugTargets.get(target.getUniqueId());
    if (ids == null || ids.isEmpty()) return Audience.empty();

    return Audience.audience(
      ids.stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .toList()
    );
  }

  @Override
  public Audience getDebuggers(@NotNull UUID targetId) {
    final var ids = this.debugTargets.get(targetId);
    if (ids == null || ids.isEmpty()) return Audience.empty();

    return Audience.audience(
      ids.stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .toList()
    );
  }

}
