package fr.dreamin.dreamapi.core.logger.writer;

import fr.dreamin.dreamapi.api.logger.DebugWriter;
import fr.dreamin.dreamapi.api.logger.LogEntry;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Debug writer implementation that outputs log entries to the console.
 *
 * @author Dreamin
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class ConsoleDebugWriter implements DebugWriter {

  private final Plugin plugin;

  private volatile boolean enabled = true;

  // ##############################################################
  // ---------------------- SERVICE METHODS -----------------------
  // ##############################################################

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void write(@NotNull LogEntry entry) {
    final var prefix = String.format(
      "[%s][%s::%s:%d]",
      entry.threadName(),
      entry.category(),
      entry.context().methodName(),
      entry.context().lineNumber()
    );

    final var line = String.format("%s - %s", prefix, entry.message());

    switch (entry.level()) {
      case ERROR -> this.plugin.getLogger().severe(line);
      case WARN -> this.plugin.getLogger().warning(line);
      default -> this.plugin.getLogger().info(line);
    }

  }
}
