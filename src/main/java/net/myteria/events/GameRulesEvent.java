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
import net.myteria.menus.GameRulesMenu;
import net.myteria.objects.PlayerWorld;
import net.myteria.utils.Scheduler;

public class GameRulesEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof GameRulesMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			UUID uuid = api.getWorldOwner(player.getWorld()).getUniqueId();
			PlayerWorld world = api.getWorldInstance(uuid);
			String selectedWorld = api.getWorldInstance(uuid).getWorldName();
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				api.gameRulesPage.replace(player, api.gameRulesPage.get(player) + 1);
				api.getGameRulesMenu().setInventory(api.gameRulesInv.get(player), api.gameRulesPage.get(player));
				return;
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.gameRulesPage.replace(player, api.gameRulesPage.get(player) - 1);
				api.getGameRulesMenu().setInventory(api.gameRulesInv.get(player), api.gameRulesPage.get(player));
				return;
			}

			// Subtract
			if (clickedItem.getType() == Material.GREEN_WOOL && event.isLeftClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() - 1) == 0) {
						clickedItem.setType(Material.RED_WOOL);
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
						return;
					} else {
						clickedItem.setType(Material.GREEN_WOOL);
						clickedItem.setAmount(clickedItem.getAmount() - 1);
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount());
						return;
					}
				}
				if (!gamerule.contains("randomTickSpeed")) {
					GameRule gameRules = GameRule.getByName(gamerule);
					clickedItem.setType(Material.RED_WOOL);
					world.setGameRule(gameRules, false);
					return;
				}
				return;
			}
			
			// Add
			if (clickedItem.getType() == Material.RED_WOOL && event.isRightClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() + 1) >= 17) {
						clickedItem.setType(Material.GREEN_WOOL);
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, 16);
						return;
					} else {
						clickedItem.setType(Material.GREEN_WOOL);
						clickedItem.setAmount(clickedItem.getAmount());
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, 1);
						return;
					}
				}
				return;
			}
			
			// Subtract
			if (clickedItem.getType() == Material.RED_WOOL && event.isLeftClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() - 1) == 0) {
						return;
					} else {
						clickedItem.setType(Material.RED_WOOL);
						clickedItem.setAmount(clickedItem.getAmount() - 1);
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount());
						return;
					}
				}
				if (!gamerule.contains("randomTickSpeed")) {
					GameRule gameRules = GameRule.getByName(gamerule);
					clickedItem.setType(Material.GREEN_WOOL);
					world.setGameRule(gameRules, true);
					return;
				}
				return;
			}
						
			// Add
			if (clickedItem.getType() == Material.GREEN_WOOL && event.isRightClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() + 1) >= 17) {
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, 16);
						return;
					} else {
						clickedItem.setAmount(clickedItem.getAmount() + 1);
						world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount());
						return;
					}
				}
				return;
			}
		}
	}
}
