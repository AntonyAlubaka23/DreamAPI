package fr.dreamin.dreamapi.core.recipe.event;

import fr.dreamin.dreamapi.api.recipe.CustomRecipe;
import fr.dreamin.dreamapi.core.event.ToolsCancelEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Getter
@RequiredArgsConstructor
public final class CustomSmithingEvent extends ToolsCancelEvent {

  private final Player player;
  private final CustomRecipe recipe;

}
