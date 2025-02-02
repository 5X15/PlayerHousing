package net.myteria.events;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.SettingsMenu;
import net.myteria.utils.Scheduler;

public class SettingsEvent implements Listener{
	
	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
		if (event.getClickedInventory().getHolder() instanceof SettingsMenu) {
			HousingAPI api = PlayerHousing.getAPI();
			event.setCancelled(true);
			ItemStack clickedItem = event.getCurrentItem();
			Player player = (Player)event.getWhoClicked();
			OfflinePlayer owner = api.getWorldOwner(player.getWorld());
			World world = player.getWorld();
			String worndName = api.getWorldConfig(owner.getUniqueId()).getString("default-world");
			String status = api.getWorldConfig(owner.getUniqueId()).getString(worndName + ".settings.status");

			// Subtract
			switch(clickedItem.getType()) {
				case SPAWNER: {
					switch(api.getWorldConfig(owner.getUniqueId()).getString(worndName + ".settings.difficulty")) {
					case "EASY": {
						api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.difficulty", "NORMAL");
						try {
							api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scheduler.runTaskLater(player, PlayerHousing.getInstance(), () -> {
							world.setDifficulty(Difficulty.NORMAL);
						}, null, 1);
						openSettingsMenu(player);
						break;
					}
					case "NORMAL": {
						api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.difficulty","HARD");
						try {
							api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scheduler.runTaskLater(player, PlayerHousing.getInstance(), () -> {
							world.setDifficulty(Difficulty.HARD);
						}, null, 1);
						openSettingsMenu(player);
						break;
					}
					case "HARD": {
						api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.difficulty", "PEACEFUL");
						try {
							api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scheduler.runTaskLater(player, PlayerHousing.getInstance(), () -> {
							world.setDifficulty(Difficulty.PEACEFUL);
						}, null, 1);
						openSettingsMenu(player);
						break;
					}
					case "PEACEFUL": {
						api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.difficulty", "EASY");
						try {
							api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Scheduler.runTaskLater(player, PlayerHousing.getInstance(), () -> {
							world.setDifficulty(Difficulty.EASY);
						}, null, 1);
						openSettingsMenu(player);
						break;
					}
				}
					break;
				}
				case CLOCK: {
					Scheduler.runTask(PlayerHousing.getInstance(), () -> {
						if (world.isDayTime()) {
							world.setTime(13000);
						} 
						if (!world.isDayTime()) {
							world.setTime(1000);
						}
					});
					openSettingsMenu(player);
					break;
				}
				case DIAMOND_SWORD: {
					Scheduler.runTask(PlayerHousing.getInstance(), () -> {
						world.setPVP(!world.getPVP());
					});
					api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.pvp", world.getPVP());
					try {
						api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					openSettingsMenu(player);
					break;
				}
				case CRAFTING_TABLE: {
					switch(api.getWorldConfig(owner.getUniqueId()).getString(worndName + ".settings.gamemode")) {
						case "ADVENTURE": {
							api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.gamemode", "CREATIVE");
							try {
								api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							for(Player players: world.getPlayers()) {
								players.setGameMode(GameMode.CREATIVE);
							}
							openSettingsMenu(player);
							break;
						}
						case "CREATIVE": {
							api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.gamemode","SURVIVAL");
							try {
								api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							for(Player players: world.getPlayers()) {
								players.setGameMode(GameMode.SURVIVAL);
							}
							openSettingsMenu(player);
							break;
						}
						case "SURVIVAL": {
							api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.gamemode", "ADVENTURE");
							try {
								api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							for(Player players: world.getPlayers()) {
								players.setGameMode(GameMode.ADVENTURE);
							}
							openSettingsMenu(player);
							break;
						}
					}
					break;
				}
				case BOOK: {
					player.closeInventory();
					api.getGameRulesMenu().setupMenu(player.getUniqueId(), true);
					api.gameRulesInv.put(player, Bukkit.createInventory(api.getGameRulesMenu(), 5*9, "GameRules"));
					
					api.getGameRulesMenu().setInventory(api.gameRulesInv.get(player), api.gameRulesPage.get(player));
					
					player.openInventory(api.getGameRulesMenu().getInventory());
					break;
					
				}
				case OAK_DOOR: {
					api.getWorldConfig(owner.getUniqueId()).set(worndName + ".settings.status", ((status == "PUBLIC") ? "PRIVATE" : "PUBLIC"));
					try {
						api.getWorldConfig(owner.getUniqueId()).save(api.getConfigManager().getFile(owner.getUniqueId()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					openSettingsMenu(player);
					break;
				}
				case TNT: {
					break;
				}
				default:
					break;
				}
		}
		
	}

	private void openSettingsMenu(Player player) {
		HousingAPI api = PlayerHousing.getAPI();
		Scheduler.runTaskLater(player, PlayerHousing.getInstance(), () -> {
			api.getSettingsMenu().setupInventory(player);
			player.openInventory(api.getSettingsMenu().getInventory());
		}, null, 1L);
	}
}
