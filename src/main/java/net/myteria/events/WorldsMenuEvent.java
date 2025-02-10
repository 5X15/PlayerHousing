package net.myteria.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.WorldsMenu;

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
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.worldsMenuPage.replace(player, api.worldsMenuPage.get(player) - 1);
				
				api.getWorldsMenu().setInventory(api.worldsMenuInv.get(player), api.worldsMenuPage.get(player));
			}

			if (clickedItem.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(PlayerHousing.getInstance(), "folder"))){
				api.getConfigManager().verifyConfig(player.getUniqueId(), "world");
				String folder = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "folder"), PersistentDataType.STRING);
				if (folder != null) {
					Path rawPreset = Paths.get(PlayerHousing.getInstance().getDataFolder() + "/presets/" + folder);
					Path rawDir = Paths.get("housing/", player.getUniqueId().toString() + "/world");
					if (!rawDir.toFile().exists()) {
						rawDir.toFile().mkdirs();
					}
					
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
					player.closeInventory();
					api.loadWorld(player.getUniqueId());
					api.joinWorld(player, player.getUniqueId());
				}

			}
			
			
		}
		
	}
}
