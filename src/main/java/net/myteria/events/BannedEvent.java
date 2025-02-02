package net.myteria.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.myteria.HousingAPI;
import net.myteria.HousingAPI.Action;
import net.myteria.PlayerHousing;
import net.myteria.menus.BannedMenu;
import net.myteria.menus.ConfirmMenu;

public class BannedEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof BannedMenu) {
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			HousingAPI api = PlayerHousing.getAPI();
			
			World world = player.getWorld();
			OfflinePlayer target = ((SkullMeta)event.getClickedInventory().getItem(13).getItemMeta()).getOwningPlayer();
			
			if (target.isOnline() && target.getPlayer().getWorld() != world) {
				Bukkit.getLogger().warning("Player performed an action but the worlds did not match! exploit? " + player.getName() + " -> Banned event");
				return;
			}
			
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
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.Ban, target).getInventory());
				return;
			}
			if (clickedItem.getType() == Material.RED_WOOL) {
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.Unban, target).getInventory());
				return;
			}
		}
		
	}
}
