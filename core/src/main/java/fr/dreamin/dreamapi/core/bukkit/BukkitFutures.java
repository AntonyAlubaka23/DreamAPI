package fr.dreamin.dreamapi.core.bukkit;

import fr.dreamin.dreamapi.api.DreamAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public final class BukkitFutures {

  public static <T> CompletableFuture<T> supplySync(
    final @NotNull Supplier<T> supplier
  ) {
    final var future = new CompletableFuture<T>();

    Bukkit.getScheduler().runTask(DreamAPI.getAPI().plugin(), () -> {
      try {
        future.complete(supplier.get());
      } catch (Throwable t) {
        future.completeExceptionally(t);
      }
    });

    return future;
  }

  public static <T> CompletableFuture<T> supplySync(
    final @NotNull Plugin plugin,
    final @NotNull Supplier<T> supplier
  ) {
    final var future = new CompletableFuture<T>();

    Bukkit.getScheduler().runTask(plugin, () -> {
      try {
        future.complete(supplier.get());
      } catch (Throwable t) {
        future.completeExceptionally(t);
      }
    });

    return future;
  }

}
