package net.myteria.events;

import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.HousingMenu;
import net.myteria.menus.WorldsMenu;

public class WorldsMenuEvent implements Listener {
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof WorldsMenu) {
			event.setCancelled(true);
			HousingAPI api = PlayerHousing.getAPI();
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

			if (clickedItem.getType() == Material.PLAYER_HEAD){
				SkullMeta meta = (SkullMeta) clickedItem.getItemMeta();
				api.joinWorld(player, meta.getOwningPlayer().getUniqueId());
			}
		}
		if (event.getClickedInventory().getHolder() instanceof HousingMenu) {
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			if (clickedItem.getType() == Material.OAK_SIGN){
				player.sendMessage("Hooked successfully");
			}
		}
		
	}
}