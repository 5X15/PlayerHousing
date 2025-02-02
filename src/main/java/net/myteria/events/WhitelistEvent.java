package net.myteria.events;

import java.io.IOException;
import java.util.UUID;

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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.ConfirmMenu;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.OnlinePlayersMenu;
import net.myteria.menus.WhitelistMenu;

public class WhitelistEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof WhitelistMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			World world = player.getWorld();
			
			OfflinePlayer target = ((SkullMeta)event.getClickedInventory().getItem(13).getItemMeta()).getOwningPlayer();
			
			if (target.isOnline() && target.getPlayer().getWorld() != world) {
				Bukkit.getLogger().warning("Player performed an action but the worlds did not match! exploit? " + player.getName() + " -> Whitelist event");
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
				player.openInventory(new ConfirmMenu(Action.addWhitelist, target).getInventory());
				return;
			}
			if (clickedItem.getType() == Material.RED_WOOL) {
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.removeWhitelist, target).getInventory());
				return;
			}
		}
		
	}
}
