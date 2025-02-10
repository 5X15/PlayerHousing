package net.myteria.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.WorldsMenu;
import net.myteria.objects.PlayerWorld;

public class WorldsMenuEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof WorldsMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				api.worldsMenuPage.replace(player, api.worldsMenuPage.get(player) + 1);
				api.getWorldsMenu().setInventory(api.worldsMenuInv.get(player), api.worldsMenuPage.get(player));
				return;
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.worldsMenuPage.replace(player, api.worldsMenuPage.get(player) - 1);
				api.getWorldsMenu().setInventory(api.worldsMenuInv.get(player), api.worldsMenuPage.get(player));
				return;
			}

			if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(PlayerHousing.getInstance(), "folder"))){
				api.getConfigManager().verifyConfig(player.getUniqueId());
				PlayerWorld world = api.getWorldInstance(player.getUniqueId());
				String folder = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "folder"), PersistentDataType.STRING);
				boolean isVoid = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "isVoid"), PersistentDataType.BOOLEAN);
				if (folder != null) {
					Path rawPreset = Paths.get(PlayerHousing.getInstance().getDataFolder() + "/presets/" + folder);
					if (!rawPreset.toFile().exists()) {
						PlayerHousing.getInstance().getLogger().warning("Template '/presets/" + folder + "' does not exist!");
						return;
					}
					Path rawDir = Paths.get(String.format("housing/%s/%s", player.getUniqueId(), world.getWorldName()));
					if (!rawDir.toFile().exists()) {
						rawDir.toFile().mkdirs();
					}
					if (rawPreset.toFile().exists()) {
						try {
				            // Copy the entire source folder to the target folder
				            Files.walk(rawPreset)
				                    .forEach(source -> {
				                        Path target = rawDir.resolve(rawPreset.relativize(source));
				                        try {
				                            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
				                        } catch (IOException e) {
				                            e.printStackTrace();
				                        }
				                    });
				        } catch (IOException e) {
				            e.printStackTrace();
				        }
					}
					if (!isVoid) {
						world.getConfig().set("isvoid", false);
						world.save(false);
					}
					player.closeInventory();
					api.loadWorld(player.getUniqueId());
					api.joinWorld(player, player.getUniqueId());
					return;
				}

			} else {
				PlayerHousing.getInstance().getLogger().warning("Failed to load template " + clickedItem.getItemMeta().getItemName());
				return;
			}
		}
	}
}
