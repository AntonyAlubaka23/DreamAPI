package fr.dreamin.example;

import fr.dreamin.dreamapi.api.item.ItemDefinition;
import fr.dreamin.dreamapi.api.item.ItemRegistryService;
import fr.dreamin.dreamapi.plugin.DreamPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;

@Getter
public final class ExamplePlugin extends DreamPlugin implements Listener {

  @Override
  public void onDreamEnable() {
    getLogger().info("DreamAPI good");

    setBroadcastCmd(true);
    setItemRegistryCmd(true);
    setDebugCmd(true);
    setServiceCmd(true);

    Bukkit.getPluginManager().registerEvents(new CuboidListener(), this);

  }

  @Override
  public void onDreamDisable() {
    getLogger().info("DreamAPI dead");
  }

}
