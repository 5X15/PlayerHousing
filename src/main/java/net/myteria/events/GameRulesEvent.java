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

public class GameRulesEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof GameRulesMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			
			World world = player.getWorld();
			UUID uuid = api.getWorldOwner(world).getUniqueId();
			String selectedWorld = api.getWorldConfig(uuid).getString("default-world");
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 44) {
				api.gameRulesPage.replace(player, api.gameRulesPage.get(player) + 1);
				
				api.getGameRulesMenu().setInventory(api.gameRulesInv.get(player), api.gameRulesPage.get(player));
			}
			if (clickedItem.getType() == Material.ARROW && event.getSlot() == 36) {
				api.gameRulesPage.replace(player, api.gameRulesPage.get(player) - 1);
				
				api.getGameRulesMenu().setInventory(api.gameRulesInv.get(player), api.gameRulesPage.get(player));
			}

			// Subtract
			if (clickedItem.getType() == Material.GREEN_WOOL && event.isLeftClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() - 1) == 0) {
						clickedItem.setType(Material.RED_WOOL);
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, 0);
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					} else {
						clickedItem.setType(Material.GREEN_WOOL);
						clickedItem.setAmount(clickedItem.getAmount() - 1);
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount()));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, clickedItem.getAmount());
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
				}

			}
			
			// Add
			if (clickedItem.getType() == Material.RED_WOOL && event.isRightClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() + 1) >= 17) {
						clickedItem.setType(Material.GREEN_WOOL);
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, 16));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, 16);
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					} else {
						clickedItem.setType(Material.GREEN_WOOL);
						clickedItem.setAmount(clickedItem.getAmount());
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount()));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, 1);
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
				}
				
			}
			
			// Subtract
			if (clickedItem.getType() == Material.RED_WOOL && event.isLeftClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() - 1) == 0) {
						clickedItem.setType(Material.GREEN_WOOL);
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, 0);
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					} else {
						clickedItem.setType(Material.RED_WOOL);
						clickedItem.setAmount(clickedItem.getAmount() - 1);
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount()));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, clickedItem.getAmount());
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
				}

			}
						
			// Add
			if (clickedItem.getType() == Material.GREEN_WOOL && event.isRightClick()){
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (gamerule.contains("randomTickSpeed")) {
					if ((clickedItem.getAmount() + 1) >= 17) {
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, 16));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, 16);
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					} else {
						clickedItem.setAmount(clickedItem.getAmount() + 1);
						Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(GameRule.RANDOM_TICK_SPEED, clickedItem.getAmount()));
						api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, clickedItem.getAmount());
						try {
							api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
				}
							
			}
			
			if (clickedItem.getType() == Material.GREEN_WOOL) {
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (!gamerule.contains("randomTickSpeed")) {
					GameRule gameRules = GameRule.getByName(gamerule);
					Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(gameRules, false));
					clickedItem.setType(Material.RED_WOOL);
					api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, false);
					try {
						api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
					
			}
			
			if (clickedItem.getType() == Material.RED_WOOL) {
				String gamerule = clickedItem.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(PlayerHousing.getInstance(), "gamerule"), PersistentDataType.STRING);
				if (!gamerule.contains("randomTickSpeed")) {
					GameRule gameRules = GameRule.getByName(gamerule);
					Bukkit.getGlobalRegionScheduler().execute(PlayerHousing.getInstance(), () -> world.setGameRule(gameRules, true));
					clickedItem.setType(Material.GREEN_WOOL);
					api.getWorldConfig(uuid).set(selectedWorld + ".gamerules." + gamerule, true);
					try {
						api.getWorldConfig(uuid).save(api.getConfigManager().getFile(uuid));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
				
			}
		}
		
	}
}
