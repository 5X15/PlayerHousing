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

import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.HousingMenu;
import net.myteria.menus.PlayerManagerMenu;
import net.myteria.menus.SettingsMenu;

public class PlayerManagerEvent implements Listener{
	HousingAPI api = PlayerHousing.getAPI();
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof PlayerManagerMenu) {
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();

			// Subtract
			switch(clickedItem.getType()) {
				case WRITABLE_BOOK: {
					player.closeInventory();
					openPlayersMenu(player, Action.Whitelist);
					break;
				}
				case BUCKET: {
					player.closeInventory();
					openPlayersMenu(player, Action.Manage);
					break;
				}
				
				case BARRIER: {
					player.closeInventory();
					openPlayersMenu(player, Action.Banned);
					break;
				}
				
				default: {
					break;
				}
			}
		}
		
	}
	public void openPlayersMenu(Player player, Action action) {
		api.getOnlinePlayersMenu().setupMenu(player, action);
		api.playersInv.put(player, Bukkit.createInventory(api.getOnlinePlayersMenu(), 5*9, "Players"));
		
		api.getOnlinePlayersMenu().setInventory(api.playersInv.get(player), api.playersPage.get(player));
		
		player.openInventory(api.getOnlinePlayersMenu().getInventory());
	}
}
