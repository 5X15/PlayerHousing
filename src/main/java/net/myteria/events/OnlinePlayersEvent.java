package net.myteria.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.OnlinePlayersMenu;

public class OnlinePlayersEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof OnlinePlayersMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				api.playersPage.replace(player, api.playersPage.get(player) + 1);
				api.getOnlinePlayersMenu().setInventory(api.playersInv.get(player), api.playersPage.get(player));
				return;
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.playersPage.replace(player, api.playersPage.get(player) - 1);
				api.getOnlinePlayersMenu().setInventory(api.playersInv.get(player), api.playersPage.get(player));
				return;
			}

			if (clickedItem.getType() == Material.PLAYER_HEAD) {
				Action action = Action.valueOf(clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING));
				switch(action) {
					case Manage:{
						api.getOptionsMenu().setupMenu(((SkullMeta) clickedItem.getItemMeta()).getOwningPlayer(), player.getWorld());
						player.openInventory(api.getOptionsMenu().getInventory());
						break;
					}
					case Whitelist:{
						api.getWhitelistMenu().setupMenu(((SkullMeta) clickedItem.getItemMeta()).getOwningPlayer());
						player.openInventory(api.getWhitelistMenu().getInventory());
						break;
					}
					case Banned:{
						api.getBannedMenu().setupMenu(((SkullMeta) clickedItem.getItemMeta()).getOwningPlayer());
						player.openInventory(api.getBannedMenu().getInventory());
						break;
					}
				default:
					break;
				}
			}
			return;
		}
		
	}
}
