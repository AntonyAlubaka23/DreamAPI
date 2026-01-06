package fr.dreamin.dreamapi.core.dependency;

import fr.dreamin.dreamapi.api.DreamAPI;
import fr.dreamin.dreamapi.api.dependency.DependencyType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DependencyReader {

  private static final Map<String, YamlConfiguration> CACHE = new ConcurrentHashMap<>();

  private static YamlConfiguration load(final @NotNull String resource) {
    final var plugin = DreamAPI.getAPI().plugin();
    final var key = plugin.getName() + ":" + resource;

    return CACHE.computeIfAbsent(key, k -> {
      try (var in = plugin.getResource(resource)) {
        if (in == null) return null;
        return YamlConfiguration.loadConfiguration(new InputStreamReader(in));
      } catch (Exception e) {
        return null;
      }
    });
  }

  // ###############################################################
  // ------------------------- PLUGIN YML --------------------------
  // ###############################################################

  public static boolean hasHardDepend(final @NotNull String dependency) {
    final var yaml = load("plugin.yml");
    if (yaml == null) return false;

    return yaml.getStringList("depend").stream()
      .anyMatch(dep -> dep.equalsIgnoreCase(dependency));
  }

  public static boolean hasSoftDepend(final @NotNull String dependency) {
    final var yaml = load("plugin.yml");
    if (yaml == null) return false;

    return yaml.getStringList("softdepend").stream()
      .anyMatch(dep -> dep.equalsIgnoreCase(dependency));
  }

  // ###############################################################
  // ---------------------- PAPER PLUGIN YML -----------------------
  // ###############################################################

  public static boolean hasPaperHard(final @NotNull String dependency) {
    final var yaml = load("paper-plugin.yml");
    if (yaml == null) return false;

    final var server = yaml.getConfigurationSection("dependencies.server");
    if (server == null) return false;

    final var dep = server.getConfigurationSection(dependency);
    if (dep == null) return false;

    return dep.getBoolean("required", false);
  }

  public static boolean hasPaperSoftAfter(final @NotNull String dependency) {
    final var yaml = load("paper-plugin.yml");
    if (yaml == null) return false;

    final var server = yaml.getConfigurationSection("dependencies.server");
    if (server == null) return false;

    final var dep = server.getConfigurationSection(dependency);
    if (dep == null) return false;

    return !dep.getBoolean("required", false) && "AFTER".equalsIgnoreCase(dep.getString("load"));
  }

  // ###############################################################
  // -------------------------- COMBINED ---------------------------
  // ###############################################################

  public static DependencyType getDeclaredType(final @NotNull String dependency) {
    if (hasPaperHard(dependency)) return DependencyType.HARD;
    if (hasPaperSoftAfter(dependency)) return DependencyType.SOFT;

    if (hasHardDepend(dependency)) return DependencyType.HARD;
    if (hasSoftDepend(dependency)) return DependencyType.SOFT;

    return DependencyType.NONE;
  }

  public static boolean isSoft(final @NotNull String dependency) {
    return getDeclaredType(dependency) == DependencyType.SOFT;
  }

  public static boolean isHard(final @NotNull String dependency) {
    return getDeclaredType(dependency) == DependencyType.HARD;
  }

}
