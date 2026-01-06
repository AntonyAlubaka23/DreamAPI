package fr.dreamin.dreamapi.core.item.ui;

import fr.dreamin.dreamapi.api.DreamAPI;
import fr.dreamin.dreamapi.api.item.ItemRegistryService;
import fr.dreamin.dreamapi.core.gui.GuiInterface;
import fr.dreamin.dreamapi.core.gui.item.NextItem;
import fr.dreamin.dreamapi.core.gui.item.PreviousItem;
import fr.dreamin.dreamapi.core.item.builder.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.Click;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.AbstractItem;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;

import java.util.ArrayList;
import java.util.List;

public final class ItemRegistryGUI implements GuiInterface {

  private final @NotNull ItemRegistryService itemRegistryService = DreamAPI.getAPI().getService(ItemRegistryService.class);

  @Override
  public Component name(@NotNull Player player) {
    return Component.text("Item Registry");
  }

  @Override
  public Gui guiUpper(@NotNull Player player) {
    return PagedGui.itemsBuilder()
      .setStructure(
        ". . . . . . . . .",
        ". X X X X X X X .",
        ". X X X X X X X .",
        ". X X X X X X X .",
        ". X X X X X X X .",
        "P . . . . . . . N"
      )
      .addIngredient('X', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
      .addIngredient('P', new PreviousItem())
      .addIngredient('N', new NextItem())
      .setContent(getItems())
      .build();
  }

  // ###############################################################
  // ----------------------- PRIVATE METHODS -----------------------
  // ###############################################################

  private List<Item> getItems() {
    final var rs = new ArrayList<Item>();

    for (final var registered : this.itemRegistryService.getAllRegisteredItems()) {
      rs.add(new AbstractItem() {
        @Override
        public @NotNull ItemProvider getItemProvider(@NotNull Player player) {
          return new ItemBuilder(registered.item()).toGuiItem();
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
          player.getInventory().addItem(registered.item());
        }
      });
    }

    return rs;
  }

}
