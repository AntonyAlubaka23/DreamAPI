package fr.dreamin.dreamapi.api.recipe;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface RecipeViewerService {

  void openFakeViewer(final @NotNull Player player, final @NotNull CustomRecipe recipe);

  void openVanillaPreview(final @NotNull Player player, final @NotNull CustomRecipe recipe);

}
