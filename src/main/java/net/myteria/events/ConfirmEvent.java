package net.myteria.events;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI;
import net.myteria.HousingAPI.Action;
import net.myteria.PlayerHousing;
import net.myteria.menus.ConfirmMenu;
import net.myteria.objects.PlayerWorld;

public class ConfirmEvent implements Listener{
	HousingAPI api = PlayerHousing.getAPI();
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof ConfirmMenu) {
			
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			OfflinePlayer owner = api.getWorldOwner(player.getWorld());;
			PlayerWorld world = api.getWorldInstance(owner.getUniqueId());
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
						boolean status = world.removePlayer(target.getUniqueId());
						if (status) {
							player.sendMessage("Kicked " + target.getName());
						} else {
							player.sendMessage("Failed to kick " + target.getName());
						}
						player.closeInventory();
						break;
					}
					case Ban:{
						boolean status = world.ban(target.getUniqueId());
						if (status) {
							player.sendMessage("Banned " + target.getName());
						} else {
							player.sendMessage("Failed to ban " + target.getName());
						}
						player.closeInventory();
						break;
					}
					case Unban:{
						boolean status = world.unban(target.getUniqueId());
						if (status) {
							player.sendMessage("Unbanned " + target.getName());
						} else {
							player.sendMessage("Failed to ban " + target.getName());
						}
						player.closeInventory();
						break;
					}
					case addWhitelist:{
						boolean status = world.whitelist(target.getUniqueId());
						if (status) {
							player.sendMessage("Whitelisted " + target.getName());
						} else {
							player.sendMessage("Failed to whitelist " + target.getName());
						}
						break;
					}
					case removeWhitelist:{
						boolean status = world.unwhitelist(target.getUniqueId());
						if (status) {
							player.sendMessage("Un-whitelisted " + target.getName());
						} else {
							player.sendMessage("Failed to un-whitelist " + target.getName());
						}
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
