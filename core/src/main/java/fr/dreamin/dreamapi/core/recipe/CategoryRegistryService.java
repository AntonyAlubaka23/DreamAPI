package fr.dreamin.dreamapi.core.recipe;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface CategoryRegistryService {

  void registerCategory(final @NotNull RecipeCategory category);

  @Nullable RecipeCategory getCategory(final @NotNull String id);

  @NotNull Collection<RecipeCategory> getAllCategories();

}
