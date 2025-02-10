package net.myteria.events;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
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
import net.myteria.menus.HousingMenu;
import net.myteria.menus.SettingsMenu;

public class HousingEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof HousingMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();

			// Subtract
			switch(clickedItem.getType()) {
				case COMMAND_BLOCK: {
					player.closeInventory();
					api.getSettingsMenu().setupInventory(player);
					player.openInventory(api.getSettingsMenu().getInventory());
					break;
				}
				case WRITABLE_BOOK: {
					player.closeInventory();
					player.openInventory(api.getPlayerManagerMenu().getInventory());
					break;
				}
				
				default:
					break;
				}
		}
		
	}
}
