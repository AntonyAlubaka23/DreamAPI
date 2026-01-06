package fr.dreamin.dreamapi.core.logger.writer;

import fr.dreamin.dreamapi.api.logger.DebugWriter;
import fr.dreamin.dreamapi.api.logger.LogEntry;
import fr.dreamin.dreamapi.api.logger.LogLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * Debug writer that sends log messages directly to the Minecraft chat
 * using Bukkit broadcast.
 *
 * @author Dreamin
 * @since 1.0.0
 */
@RequiredArgsConstructor
public final class BroadcastDebugWriter implements DebugWriter {

  private final @NotNull Plugin plugin;

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
    final var ctx = entry.context();

    final var msg = Component.text("[DEBUG] ", NamedTextColor.DARK_GRAY)
      .append(Component.text(String.format("[%s][%s]", entry.level().name(), entry.threadName()), NamedTextColor.DARK_GRAY))
      .append(
        Component.text(
          "[" + entry.category() + "::" + ctx.methodName() + ":" + ctx.lineNumber() + "] ",
          NamedTextColor.DARK_GRAY
        )
      )
      .append(Component.text(entry.message(), colorForLevel(entry.level())));

    Bukkit.broadcast(msg);
  }

  // ###############################################################
  // ----------------------- PRIVATE METHODS -----------------------
  // ###############################################################

  private NamedTextColor colorForLevel(final @NotNull LogLevel level) {
    return switch (level) {
      case ERROR -> NamedTextColor.RED;
      case WARN -> NamedTextColor.YELLOW;
      case INFO -> NamedTextColor.WHITE;
      case DEBUG -> NamedTextColor.AQUA;
      case TRACE -> NamedTextColor.LIGHT_PURPLE;
    };
  }

}
