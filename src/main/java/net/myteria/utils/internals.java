package net.myteria.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.util.TriState;
import net.myteria.PlayerHousing;
import net.myteria.HousingAPI.Action;
import net.myteria.menus.BannedMenu;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.HousingMenu;
import net.myteria.menus.OnlinePlayersMenu;
import net.myteria.menus.OptionsMenu;
import net.myteria.menus.PlayerManagerMenu;
import net.myteria.menus.SettingsMenu;
import net.myteria.menus.WhitelistMenu;
import net.myteria.menus.WorldsMenu;

public final class internals {
	protected HashMap<OfflinePlayer, World> worlds = new HashMap<>();
	protected HashMap<UUID, YamlConfiguration> worldConfigs = new HashMap<>();
	protected final ConfigManager configManager;
	protected final WorldUtils worldUtils;
	protected final PlayerHousing instance;
	public internals(PlayerHousing plugin) {
		this.configManager = new ConfigManager();
		this.worldUtils = plugin.isFolia() ? new WorldUtils() : null;
		this.instance = plugin;
	}

    public GameRulesMenu getGameRulesMenu() {
        return instance.gameRulesMenu;
    }

    public SettingsMenu getSettingsMenu() {
        return instance.settingsMenu;
    }

    public HousingMenu getHousingMenu() {
        return instance.housingMenu;
    }

    public OnlinePlayersMenu getOnlinePlayersMenu() {
        return instance.playersMenu;
    }

    public PlayerManagerMenu getPlayerManagerMenu() {
        return instance.managerMenu;
    }

    public WhitelistMenu getWhitelistMenu() {
        return instance.whitelistMenu;
    }

    public OptionsMenu getOptionsMenu() {
        return instance.optionsMenu;
    }

    public BannedMenu getBannedMenu() {
        return instance.bannedMenu;
    }

    public WorldsMenu getWorldsMenu() {
        return instance.worldsMenu;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HashMap<UUID, YamlConfiguration> getWorldConfigs() {
    	return worldConfigs;
    }

    public <T> List<List<T>> listToPages(List<T> list, int size) {
        List<List<T>> sublists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int endIndex = Math.min(i + size, list.size());
            sublists.add(list.subList(i, endIndex));
        }
        return sublists;
    }

    public YamlConfiguration getWorldConfig(UUID uuid) {
        return worldConfigs.get(uuid);
    }

    public String getSelectedWorldName(UUID uuid) {
        return getWorldConfig(uuid).getString("default-world");
    }

    public YamlConfiguration addWorldConfig(UUID uuid, YamlConfiguration config) {
        return worldConfigs.put(uuid, config);
	}

	public OfflinePlayer getWorldOwner(World world) {
		return Bukkit.getOfflinePlayer(UUID.fromString(world.getName().split("/")[1]));
	}

	public void CreateWorld(UUID uuid, String worldName, Boolean isvoid) {
		WorldCreator Creator = new WorldCreator(String.format("housing/%s/%s", uuid, worldName));
		Creator.generateStructures(false);
		if (isvoid) {
			Creator.generator(new VoidWorld());
		} else {
			Creator.generator(Bukkit.getWorld("world").getGenerator());
		}
		Creator.environment(Environment.NORMAL);
		Creator.seed(0);
		Creator.keepSpawnLoaded(TriState.TRUE);
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		World world = instance.isFolia() ? worldUtils.createWorld(Creator) : Bukkit.createWorld(Creator);
		if (world != null) {
			addWorld(player, world);
		} else {
			Bukkit.getLogger().warning("Failed to create world for " + player.getName());
		}
		
	}

	public void CreateWorld(UUID uuid, String worldName, int seed, boolean isVoid, boolean keepSpawnLoaded, boolean generateStructures, Environment environment) {
		WorldCreator Creator = new WorldCreator(String.format("housing/%s/%s", uuid, worldName));
		if (isVoid) {
			Creator.generator(new VoidWorld());
		} else {
			Creator.generator(Bukkit.getWorld("world").getGenerator());
		}
		Creator.generateStructures(generateStructures);
		Creator.environment(environment);
		Creator.seed(seed);
		Creator.keepSpawnLoaded(TriState.byBoolean(keepSpawnLoaded));
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		World world = instance.isFolia() ? worldUtils.createWorld(Creator) : Bukkit.createWorld(Creator);
		if (world != null) {
			addWorld(player, world);
		} else {
			Bukkit.getLogger().warning("Failed to create world for " + player.getName());
		}
		
	}

	public void unloadWorld(UUID uuid) {
		World world = getWorld(uuid);
		Scheduler.runTask(instance, world.getSpawnLocation(), () -> world.save());
		if (instance.isFolia()) {
			worldUtils.unloadWorld(world);
		} else {
			Bukkit.unloadWorld(world, true);
		}
		
	}

	public World getWorld(UUID uuid) {
		if (getConfigManager().hasWorld(uuid) && worlds.get(Bukkit.getOfflinePlayer(uuid)) == null) {
			addWorld(Bukkit.getOfflinePlayer(uuid), Bukkit.getWorld(String.format("housing/%s/%s", uuid, getWorldConfig(uuid).getString("default-world"))));
		}
		return worlds.get(Bukkit.getOfflinePlayer(uuid));
	}

	public HashMap<OfflinePlayer, World> getWorlds() {
		return worlds;
	}

	public void joinWorld(Player player, UUID uuid) {
		if (!getConfigManager().hasWorld(uuid)) {
			player.sendMessage("This player does not have a world!");
			return;
		}
		if (getWorld(uuid) == null) {
			player.sendMessage("This world is offline!");
			return;
		}
		String worldName = getWorldNameFromWorld(getWorld(uuid));
		
		if (getWorldConfig(uuid).getList(worldName + ".banned").contains(player.getUniqueId().toString())) {
			player.sendMessage("You are banned on this world!");
			return;
		}
		
		if (getWorldConfig(uuid).getString(worldName + ".settings.status").contains("PRIVATE") && player.getUniqueId() != uuid) {
			if (!getWorldConfig(uuid).getList(worldName + ".whitelist").contains(player.getUniqueId().toString())) {
				player.sendMessage("You are not whitelisted on this world!");
				return;
			}
		}
		Scheduler.runTaskLater(instance, getWorld(uuid).getSpawnLocation(), () -> {
			if (instance.isFolia()) {
			    player.teleportAsync(getWorld(uuid).getSpawnLocation().toBlockLocation());
			} else {
				player.teleport(getWorld(uuid).getSpawnLocation().toBlockLocation());
			}
			player.setGameMode(GameMode.valueOf(getWorldConfig(uuid).getString(worldName + ".settings.gamemode")));
		}, 5L);
	}

	public void addWorld(@NotNull OfflinePlayer offlinePlayer, World world) {
		worlds.put(offlinePlayer, world);
	}

	public void removeWorld(World world) {
		OfflinePlayer owner = getWorldOwner(world);
		worlds.remove(owner);
	}

	@Deprecated
	public WorldUtils getWorldUtils() {
		return worldUtils;
	}

	public void loadWorld(UUID uuid) {
		if (getConfigManager().hasWorld(uuid)) {
			getConfigManager().verifyConfig(uuid, getWorldConfig(uuid).getString("default-world"));
			CreateWorld(uuid, getWorldConfig(uuid).getString("default-world"), getWorldConfig(uuid).getBoolean("isvoid"));
			return;
		}
	}

	public void performAction(Player source, OfflinePlayer target, Action action) {
		
		UUID uuid = getWorldOwner(source.getWorld()).getUniqueId();
		if (uuid == target.getUniqueId()) {
			source.sendMessage("You can not perform actions against the world owner!");
			return;
		}
		String selectedWorld = getWorldConfig(uuid).getString("default-world");
		switch(action) {
			case Kick:{
				if (instance.isFolia()) {
					target.getPlayer().teleportAsync(Bukkit.getWorld("world").getSpawnLocation().toBlockLocation());
				} else {
					target.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation().toBlockLocation());
				}
				target.getPlayer().sendMessage("You have been kicked from this world!");
				source.sendMessage("Kicked player!");
				break;
			}
			case Ban:{
				
				if(getWorldConfig(uuid).getList(selectedWorld + ".banned").contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is already banned!");
					source.closeInventory();
					break;
				}
				List<String> whitelist = getWorldConfig(uuid).getStringList(selectedWorld + ".banned");
				whitelist.add(target.getUniqueId().toString());
				getWorldConfig(uuid).set(selectedWorld + ".banned", whitelist);
				try {
					getWorldConfig(uuid).save(getConfigManager().getFile(uuid));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (instance.isFolia()) {
					target.getPlayer().teleportAsync(Bukkit.getWorld("world").getSpawnLocation().toBlockLocation());
				} else {
					target.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation().toBlockLocation());
				}
				target.getPlayer().sendMessage("You have been banned from this world!");
				source.sendMessage("Banned player!");
				break;
			}
			case Unban:{
				if(!getWorldConfig(uuid).getList(selectedWorld + ".banned").contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is not banned!");
					source.closeInventory();
					break;
				}
				List<String> whitelist = getWorldConfig(uuid).getStringList(selectedWorld + ".banned");
				whitelist.remove(target.getUniqueId().toString());
				getWorldConfig(uuid).set(selectedWorld + ".banned", whitelist);
				try {
					getWorldConfig(uuid).save(getConfigManager().getFile(uuid));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				source.sendMessage("Unbanned player!");
				break;
			}
			case addWhitelist: {
				if(getWorldConfig(uuid).getList(selectedWorld + ".whitelist").contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is already whitelisted!");
					source.closeInventory();
					break;
				}
				List<String> whitelist = getWorldConfig(uuid).getStringList(selectedWorld + ".whitelist");
				whitelist.add(target.getUniqueId().toString());
				getWorldConfig(uuid).set(selectedWorld + ".whitelist", whitelist);
				try {
					getWorldConfig(uuid).save(getConfigManager().getFile(uuid));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				source.sendMessage("Updated whitelisted!");
				break;
			}
			case removeWhitelist: {
				if(!getWorldConfig(uuid).getList(selectedWorld + ".whitelist").contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is not whitelisted!");
					source.closeInventory();
					break;
				}
				List<String> whitelist = getWorldConfig(uuid).getStringList(selectedWorld + ".whitelist");
				whitelist.remove(target.getUniqueId().toString());
				getWorldConfig(uuid).set(selectedWorld + ".whitelist", whitelist);
				try {
					getWorldConfig(uuid).save(getConfigManager().getFile(uuid));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				source.sendMessage("Updated whitelist!");
				break;
			}
		default:
			break;
		}
		
	}

	public String getWorldNameFromWorld(World world) {
		return world.getName().split("/")[2];
	}

	public int[] getPluginVersion() {    	
    	String[] version = instance.getDescription().getVersion().split("-")[0].split("\\.");

        int[] intArray = new int[version.length];
        for (int i = 0; i < version.length; i++) {
            intArray[i] = Integer.parseInt(version[i]);
        }
        return intArray;
    }
}