package net.myteria.events;

import java.io.IOException;
import java.util.UUID;

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
import net.myteria.HousingAPI.Action;
import net.myteria.menus.ConfirmMenu;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.PermissionsMenu;

public class PermissionsEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof PermissionsMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			World world = player.getWorld();
			UUID uuid = api.getWorldOwner(world).getUniqueId();
			String selectedWorld = api.getWorldConfig(uuid).getString("default-world");
			
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				api.permissionsPage.replace(player, api.permissionsPage.get(player) + 1);
				
				api.getPermissionsMenu().setInventory(api.permissionsInv.get(player), api.permissionsPage.get(player));
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.permissionsPage.replace(player, api.permissionsPage.get(player) - 1);
				
				api.getPermissionsMenu().setInventory(api.permissionsInv.get(player), api.permissionsPage.get(player));
			}
			String rank = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "rank"), PersistentDataType.STRING);
			String perms = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "permission"), PersistentDataType.STRING);
			if (clickedItem.getType() == Material.GREEN_WOOL) {
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.removeGroupPermission, api.inventoryTarget.get(player), rank, perms).getInventory());
			}
			if (clickedItem.getType() == Material.RED_WOOL) {
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.addGroupPermission, api.inventoryTarget.get(player), rank, perms).getInventory());
			}
			api.inventoryTarget.remove(player);
		}
		
	}
}
