package fr.dreamin.dreamapi.plugin.cmd.admin.debug;

import cloud.commandframework.annotations.suggestions.Suggestions;
import cloud.commandframework.context.CommandContext;
import fr.dreamin.dreamapi.api.DreamAPI;
import fr.dreamin.dreamapi.api.logger.DebugService;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class DebugSuggestCmd {

  private final DebugService debug = DreamAPI.getAPI().getService(DebugService.class);

  @Suggestions("debug-writers")
  public List<String> suggestWriters(CommandContext<CommandSender> sender, String input) {
    return debug.getWriters().stream()
      .map(w -> w.getClass().getSimpleName())
      .toList();
  }

  @Suggestions("debug-categories")
  public List<String> suggestCategories(CommandContext<CommandSender> sender, String input) {
    if (this.debug.getCategories().isEmpty()) return List.of();
    return this.debug.getCategories().keySet().stream().toList();
  }

}
