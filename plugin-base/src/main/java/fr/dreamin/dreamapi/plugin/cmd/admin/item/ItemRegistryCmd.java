package fr.dreamin.dreamapi.plugin.cmd.admin.item;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.CommandPermission;
import fr.dreamin.dreamapi.core.item.ui.ItemRegistryGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class ItemRegistryCmd {

  @CommandDescription("Open GUi")
  @CommandMethod("itemregistry gui")
  @CommandPermission("dreamapi.cmd.itemregistry")
  private void openGUI(CommandSender sender) {
    if (!(sender instanceof Player player)) return;

    new ItemRegistryGUI().open(player);
  }

}
