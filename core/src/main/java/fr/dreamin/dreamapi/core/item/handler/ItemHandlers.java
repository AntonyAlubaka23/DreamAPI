package fr.dreamin.dreamapi.core.item.handler;

import fr.dreamin.dreamapi.api.item.ItemHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class ItemHandlers {

  public static ItemHandler permission(final @NotNull String perm) {
    return new PermissionHandler(perm);
  }

  public static ItemHandler permission(final @NotNull String perm, final @NotNull Component noPermissionMessage) {
    return new PermissionHandler(perm, noPermissionMessage);
  }

  public static ItemHandler cooldown(final long seconds) {
    return new CooldownHandler(Duration.ofSeconds(seconds));
  }

  public static ItemHandler cooldown(final long seconds, final @NotNull Component noCooldownMessage) {
    return new CooldownHandler(Duration.ofSeconds(seconds), noCooldownMessage);
  }

  public static ItemHandler cancel() {
    return ctx -> {
      if (ctx.event() instanceof Cancellable cancellable)
        cancellable.setCancelled(true);
      return true;
    };
  }

}
