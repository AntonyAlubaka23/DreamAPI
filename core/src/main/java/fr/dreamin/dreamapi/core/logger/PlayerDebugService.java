package fr.dreamin.dreamapi.core.logger;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerDebugService {

  void startDebug(final @NotNull Player target, final @NotNull Player executor);
  void stopDebug(final @NotNull Player target, final @NotNull Player executor);

  boolean isDebugging(final @NotNull Player target, final @NotNull Player executor);

  Audience getDebuggers(final @NotNull Player target);

  Audience getDebuggers(final @NotNull UUID targetId);

}
