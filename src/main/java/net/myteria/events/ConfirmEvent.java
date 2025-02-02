package net.myteria.events;

import java.io.IOException;
import java.util.List;

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
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI;
import net.myteria.HousingAPI.Action;
import net.myteria.PlayerHousing;
import net.myteria.menus.ConfirmMenu;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.OnlinePlayersMenu;

public class ConfirmEvent implements Listener{
	HousingAPI api = PlayerHousing.getAPI();
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof ConfirmMenu) {
			
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			SkullMeta meta = (SkullMeta) event.getInventory().getItem(13).getItemMeta();
			OfflinePlayer target = meta.getOwningPlayer();
			
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

			if (clickedItem.getType() == Material.GREEN_WOOL) {
				Action action = Action.valueOf(clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "action"), PersistentDataType.STRING));
			
				switch(action) {
					case Kick:{
						api.performAction(player, target, Action.Kick);
						player.closeInventory();
						break;
					}
					case Ban:{
						api.performAction(player, target, Action.Ban);
						player.closeInventory();
						break;
					}
					case Unban:{
						api.performAction(player, target, Action.Unban);
						player.closeInventory();
						break;
					}
					case addWhitelist:{
						api.performAction(player, target, Action.addWhitelist);
						player.closeInventory();
						break;
					}
					case removeWhitelist:{
						api.performAction(player, target, Action.removeWhitelist);
						player.closeInventory();
						break;
					}
					default:
						break;
				}
				return;
			}
			if (clickedItem.getType() == Material.RED_WOOL) {
				player.closeInventory();
				return;
			}
		}
		
	}

}
