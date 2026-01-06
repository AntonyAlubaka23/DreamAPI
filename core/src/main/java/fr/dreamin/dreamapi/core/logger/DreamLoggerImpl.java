package fr.dreamin.dreamapi.core.logger;

import fr.dreamin.dreamapi.api.logger.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * Implementation of the DreamLogger interface for logging messages
 * with various log levels and categories.
 *
 * @author Dreamin
 * @since 1.0.0
 */
public final class DreamLoggerImpl implements DreamLogger {

  private final @NotNull Plugin plugin;
  private final @NotNull DebugService debugService;
  private final @NotNull String category;
  private final @NotNull Object ownerInsance;

  private @Nullable Supplier<DebugMeta> annotationTargetSupplier;

  public DreamLoggerImpl(final @NotNull Plugin plugin, final @NotNull DebugService debugService, final @NotNull String category, final @NotNull Object ownerInstance) {
    this.plugin = plugin;
    this.debugService = debugService;
    this.category = category;
    this.ownerInsance = ownerInstance;

    this.debugService.setCategory(this.category, false);

    scanForDebugTargetFields();
  }

  public DreamLoggerImpl(final @NotNull Plugin plugin, final @NotNull DebugService debugService, final @NotNull String category, final boolean enabled, final @NotNull Object ownerInsance) {
    this.plugin = plugin;
    this.debugService = debugService;
    this.category = category;
    this.ownerInsance = ownerInsance;

    this.debugService.setCategory(this.category, enabled);
  }

  // ##############################################################
  // ---------------------- SERVICE METHODS -----------------------
  // ##############################################################

  @Override
  public void info(final @NotNull DebugMeta meta, @NotNull String msg, Object... args) {
    log(LogLevel.INFO, meta, msg, args);
  }

  @Override
  public void warn(final @NotNull DebugMeta meta, @NotNull String msg, Object... args) {
    log(LogLevel.WARN, meta, msg, args);
  }

  @Override
  public void error(final @NotNull DebugMeta meta, @NotNull String msg, Object... args) {
    log(LogLevel.ERROR, meta, msg, args);
  }

  @Override
  public void debug(final @NotNull DebugMeta meta, @NotNull String msg, Object... args) {
    log(LogLevel.DEBUG, meta, msg, args);
  }

  @Override
  public void trace(final @NotNull DebugMeta meta, @NotNull String msg, Object... args) {
    log(LogLevel.TRACE, meta, msg, args);
  }

  @Override
  public void debug(@NotNull String msg, Object... args) {
    log(LogLevel.DEBUG, DebugMeta.none(), msg, args);
  }

  @Override
  public void info(@NotNull String msg, Object... args) {
    log(LogLevel.INFO, DebugMeta.none(), msg, args);
  }

  @Override
  public void warn(@NotNull String msg, Object... args) {
    log(LogLevel.WARN, DebugMeta.none(), msg, args);
  }

  @Override
  public void error(@NotNull String msg, Object... args) {
    log(LogLevel.ERROR, DebugMeta.none(), msg, args);
  }

  @Override
  public boolean isDebugEnabled() {
    return this.debugService.isGlobalDebug() || this.debugService.isCategoryEnabled(this.category);
  }

  // ###############################################################
  // ----------------------- PRIVATE METHODS -----------------------
  // ###############################################################

  /**
   * Logs a message at the specified log level.
   *
   * @param level the log level
   * @param msg the message to log
   * @param args the arguments to format the message
   *
   * @author Dreamin
   * @since 1.0.0
   */
  private void log(final @NotNull LogLevel level, final @NotNull DebugMeta explicitMeta, final @NotNull String msg, final Object... args) {
    if (!isDebugEnabled()) return;

    final var meta = explicitMeta.hasTarget()
      ? explicitMeta
      : this.annotationTargetSupplier != null
        ? this.annotationTargetSupplier.get()
        : DebugMeta.none();

    final var formatted = format(msg, args);

    final var entry = new LogEntry(
      LocalDateTime.now(),
      level,
      this.plugin.getName(),
      this.category,
      Thread.currentThread().getName(),
      formatted,
      meta,
      DebugContext.auto(this.ownerInsance, 4)
    );

    this.debugService.log(entry);
  }

  /**
   * Formats a message with the given arguments.
   * Supports both "{}" placeholders and standard String.format syntax.
   *
   * @param msg the message to format
   * @param args the arguments to format the message
   * @return the formatted message
   *
   * @author Dreamin
   * @since 1.0.0
   */
  private String format(final @NotNull String msg, final Object... args) {
    if (args == null || args.length == 0) return msg;

    if (msg.contains("{}"))
      return String.format(msg.replace("{}", "%s"), args);

    return String.format(msg, args);
  }

  private void scanForDebugTargetFields() {
    for (final var field : this.ownerInsance.getClass().getDeclaredFields()) {
      if (!field.isAnnotationPresent(DebugTarget.class)) continue;
      field.setAccessible(true);

      this.annotationTargetSupplier = () -> {
        try {
          final var value = field.get(this.ownerInsance);
          if (value instanceof Player p)
            return DebugMeta.of(p);
        } catch (Exception ignored) { }
        return DebugMeta.none();
      };
      break;
    }
  }

}
