package net.myteria.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.util.TriState;
import net.myteria.PlayerHousing;
import net.myteria.HousingAPI.Action;
import net.myteria.HousingAPI.Status;
import net.myteria.menus.*;
import net.myteria.objects.PlayerWorld;

public final class internals {
	protected HashMap<OfflinePlayer, PlayerWorld> worlds = new HashMap<>();
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

    public TemplatesMenu getTemplatesMenu() {
        return instance.templatesMenu;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public <T> List<List<T>> listToPages(List<T> list, int size) {
        List<List<T>> sublists = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int endIndex = Math.min(i + size, list.size());
            sublists.add(list.subList(i, endIndex));
        }
        return sublists;
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
			addWorldInstance(player, new PlayerWorld(player));
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
			addWorldInstance(player, new PlayerWorld(player));
		} else {
			Bukkit.getLogger().warning("Failed to create world for " + player.getName());
		}
		
	}

	public void unloadWorld(UUID uuid) {
		World world = getWorldInstance(uuid).getWorld();
		Scheduler.runTask(instance, world.getSpawnLocation(), () -> world.save());
		if (instance.isFolia()) {
			worldUtils.unloadWorld(getWorldInstance(uuid));
		} else {
			Bukkit.unloadWorld(world, true);
		}
		
	}

	public PlayerWorld getWorldInstance(UUID uuid) {
		if (getConfigManager().hasWorld(uuid) && worlds.get(Bukkit.getOfflinePlayer(uuid)) == null) {
			addWorldInstance(Bukkit.getOfflinePlayer(uuid), new PlayerWorld(Bukkit.getOfflinePlayer(uuid)));
		}
		return worlds.get(Bukkit.getOfflinePlayer(uuid));
	}
	
	public Entry<OfflinePlayer, PlayerWorld> getWorldObjectByWorld(World world) {
		for (Entry<OfflinePlayer, PlayerWorld> entry : worlds.entrySet()) {
			if (entry.getValue().getWorld() == world) {
				return entry;
			}
		}
		return null;
	}

	public HashMap<OfflinePlayer, PlayerWorld> getWorlds() {
		return worlds;
	}

	public void joinWorld(Player player, UUID uuid) {
		if (!getConfigManager().hasWorld(uuid)) {
			player.sendMessage("This player does not have a world!");
			return;
		}
		PlayerWorld world = getWorldInstance(uuid);
		if (getWorldInstance(uuid) == null) {
			player.sendMessage("This world is offline!");
			return;
		}
		
		if (world.getBanList().contains(player.getUniqueId().toString())) {
			player.sendMessage("You are banned on this world!");
			return;
		}
		
		if (world.getStatus() == Status.PRIVATE && player.getUniqueId() != uuid) {
			if (!world.getWhitelist().contains(player.getUniqueId().toString())) {
				player.sendMessage("You are not whitelisted on this world!");
				return;
			}
		}
		Scheduler.runTaskLater(instance, world.getWorld().getSpawnLocation(), () -> {
			if (instance.isFolia()) {
			    player.teleportAsync(world.getWorld().getSpawnLocation().toBlockLocation());
			} else {
				player.teleport(world.getWorld().getSpawnLocation().toBlockLocation());
			}
			player.setGameMode(world.getGamemode());
		}, 5L);
	}

	public void addWorldInstance(@NotNull OfflinePlayer offlinePlayer, PlayerWorld world) {
		worlds.put(offlinePlayer, world);
	}

	public void removeWorld(PlayerWorld world) {
		OfflinePlayer owner = world.getWorldOwner();
		worlds.remove(owner);
	}

	@Deprecated
	public WorldUtils getWorldUtils() {
		return worldUtils;
	}

	public void loadWorld(UUID uuid) {
		if (getConfigManager().hasWorld(uuid)) {
			getConfigManager().verifyConfig(uuid);
			CreateWorld(uuid, getWorldInstance(uuid).getWorldName(), getWorldInstance(uuid).getConfig().getBoolean("isvoid"));
			return;
		}
	}

	public void performAction(Player source, OfflinePlayer target, Action action) {
		
		OfflinePlayer owner = getWorldObjectByWorld(source.getWorld()).getKey();
		PlayerWorld world = getWorldObjectByWorld(source.getWorld()).getValue();
		if (owner.getUniqueId() == target.getUniqueId()) {
			source.sendMessage("You can not perform actions against the world owner!");
			return;
		}
		switch(action) {
			case Kick:{
				if (instance.isFolia()) {
					target.getPlayer().teleportAsync(world.getWorld().getSpawnLocation().toBlockLocation());
				} else {
					target.getPlayer().teleport(world.getWorld().getSpawnLocation().toBlockLocation());
				}
				target.getPlayer().sendMessage("You have been kicked from this world!");
				source.sendMessage("Kicked player!");
				break;
			}
			case Ban:{
				
				if(world.getBanList().contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is already banned!");
					source.closeInventory();
					break;
				}
				world.ban(target.getUniqueId());
				if (instance.isFolia()) {
					target.getPlayer().teleportAsync(world.getWorld().getSpawnLocation().toBlockLocation());
				} else {
					target.getPlayer().teleport(world.getWorld().getSpawnLocation().toBlockLocation());
				}
				target.getPlayer().sendMessage("You have been banned from this world!");
				source.sendMessage("Banned player!");
				break;
			}
			case Unban:{
				if(!world.getBanList().contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is not banned!");
					source.closeInventory();
					break;
				}
				world.unban(target.getUniqueId());
				source.sendMessage("Unbanned player!");
				break;
			}
			case addWhitelist: {
				if(world.getWhitelist().contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is already whitelisted!");
					source.closeInventory();
					break;
				}
				world.whitelist(target.getUniqueId());
				source.sendMessage("Updated whitelisted!");
				break;
			}
			case removeWhitelist: {
				if(!world.getWhitelist().contains(target.getUniqueId().toString())) {
					source.sendMessage("This player is not whitelisted!");
					source.closeInventory();
					break;
				}
				world.unwhitelist(target.getUniqueId());
				source.sendMessage("Updated whitelist!");
				break;
			}
		default:
			break;
		}
		
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