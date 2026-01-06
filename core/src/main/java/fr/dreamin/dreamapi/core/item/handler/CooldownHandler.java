package fr.dreamin.dreamapi.core.item.handler;

import fr.dreamin.dreamapi.api.item.ItemContext;
import fr.dreamin.dreamapi.api.item.ItemHandler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
public final class CooldownHandler implements ItemHandler {

  private final @NotNull Duration cooldown;
  private final HashMap<UUID, Instant> lastUse = new HashMap<>();

  private Component noCooldownMessage;

  public CooldownHandler(final @NotNull Duration cooldown, final @NotNull Component noCooldownMessage) {
    this.cooldown = cooldown;
    this.noCooldownMessage = noCooldownMessage;
  }

  @Override
  public boolean handle(@NotNull ItemContext ctx) {
    final var player = ctx.player();
    final var now = Instant.now();
    final var last = this.lastUse.get(player.getUniqueId());

    if (last != null && Duration.between(last, now).compareTo(this.cooldown) < 0) {
      if (ctx.event() instanceof Cancellable cancellable)
        cancellable.setCancelled(true);
      if (this.noCooldownMessage != null)
        player.sendMessage(noCooldownMessage);
      return true;
    }

    this.lastUse.put(player.getUniqueId(), now);
    player.setCooldown(ctx.item(), (int) cooldown.toMillis() / 50);
    return false;
  }
}
