package fr.dreamin.dreamapi.core.item.utils;

import fr.dreamin.dreamapi.api.DreamAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class for handling ItemStack tags using PersistentDataContainer.
 * Provides methods to check for the existence of tags and retrieve their values.
 * @see ItemStack
 * @see PersistentDataType
 * @see NamespacedKey
 * @see DreamAPI
 *
 * @author Dreamin
 * @since 1.0.0
 */
public final class ItemUtils {

  public static <T, Z> boolean hasTag(@NotNull ItemStack item, @NotNull String key, @NotNull PersistentDataType<T, Z> type) {
    if (!item.hasItemMeta()) return false;
    final var meta = item.getItemMeta();
    if (meta == null) return false;
    final var namespacedKey = new NamespacedKey(DreamAPI.getAPI().plugin(), key);
    return meta.getPersistentDataContainer().has(namespacedKey, type);
  }

  @Nullable
  public static String getStringTag(@NotNull ItemStack item, @NotNull String key) {
    if (!item.hasItemMeta()) return null;
    final var meta = item.getItemMeta();
    if (meta == null) return null;
    final var namespacedKey = new NamespacedKey(DreamAPI.getAPI().plugin(), key);
    return meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.STRING);
  }


  public static boolean getBooleanTag(@NotNull ItemStack item, @NotNull String key) {
    if (!item.hasItemMeta()) return false;
    final var meta = item.getItemMeta();
    if (meta == null) return false;
    final var namespacedKey = new NamespacedKey(DreamAPI.getAPI().plugin(), key);
    final var result = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.BOOLEAN);
    return result != null && result;
  }

  public static int getIntTag(@NotNull ItemStack item, @NotNull String key) {
    if (!item.hasItemMeta()) return -1;
    final var meta = item.getItemMeta();
    if (meta == null) return -1;
    var namespacedKey = new NamespacedKey(DreamAPI.getAPI().plugin(), key);
    final var result = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.INTEGER);
    return result != null ? result : -1;
  }

  public static double getDoubleTag(@NotNull ItemStack item, @NotNull String key) {
    if (!item.hasItemMeta()) return -1;
    final var meta = item.getItemMeta();
    if (meta == null) return -1;
    final var namespacedKey = new NamespacedKey(DreamAPI.getAPI().plugin(), key);
    final var result = meta.getPersistentDataContainer().get(namespacedKey, PersistentDataType.DOUBLE);
    return result != null ? result : -1;
  }

}
