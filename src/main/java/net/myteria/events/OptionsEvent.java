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
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.ConfirmMenu;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.OnlinePlayersMenu;
import net.myteria.menus.OptionsMenu;
import net.myteria.menus.WhitelistMenu;

public class OptionsEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof OptionsMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			World world = player.getWorld();
			UUID uuid = api.getWorldOwner(world).getUniqueId();
			Player target = (Player) ((SkullMeta)event.getClickedInventory().getItem(4).getItemMeta()).getOwningPlayer();
			String selectedWorld = api.getWorldConfig(uuid).getString("default-world");
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				api.playersPage.replace(player, api.playersPage.get(player) + 1);
				
				api.getOnlinePlayersMenu().setInventory(api.playersInv.get(player), api.playersPage.get(player));
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.playersPage.replace(player, api.playersPage.get(player) - 1);
				
				api.getOnlinePlayersMenu().setInventory(api.playersInv.get(player), api.playersPage.get(player));
			}

			if (clickedItem.getType() == Material.OAK_DOOR) {
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.kick, target, null, null).getInventory());
			}
			if (clickedItem.getType() == Material.TNT) {
				player.closeInventory();
				player.openInventory(new ConfirmMenu(Action.ban, target, null, null).getInventory());
			}
			if (clickedItem.getType() == Material.NAME_TAG) {
				if (api.permissionsInv.get(player) == null) {
					api.permissionsInv.put(player, Bukkit.createInventory(api.getPermissionsMenu(), 3*9, "Permissions Manager"));
				}
				api.inventoryTarget.put(player, target);
				api.getPermissionsMenu().setInventory(api.permissionsInv.get(player), api.permissionsPage.get(player));
				api.getPermissionsMenu().setupMenu(target.getUniqueId(), api.getPlayerRank(world, target), true);
				player.closeInventory();
				player.openInventory(api.getPermissionsMenu().getInventory());
			}
		}
		
	}
}
