package fr.dreamin.dreamapi.core.recipe.event;

import fr.dreamin.dreamapi.api.recipe.CustomRecipe;
import fr.dreamin.dreamapi.core.event.ToolsCancelEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public final class CustomCraftPreEvent extends ToolsCancelEvent {

  private final @NotNull Player player;
  private final @NotNull CustomRecipe recipe;

}
