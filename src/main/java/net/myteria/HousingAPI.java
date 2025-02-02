package net.myteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import net.myteria.menus.*;
import net.myteria.utils.ConfigManager;
import net.myteria.utils.WorldUtils;
import net.myteria.utils.internals;

public class HousingAPI {
	private final internals api;
	public HousingAPI(PlayerHousing plugin) {
		this.api = new internals(plugin);
	}
	
	public HashMap<Player, Integer> gameRulesPage = new HashMap<>();
    public HashMap<Player, Player> inventoryTarget = new HashMap<>();
    public HashMap<Player, Inventory> gameRulesInv = new HashMap<>();
    public HashMap<Player, Integer> worldsMenuPage = new HashMap<>();
    public HashMap<Player, Inventory> worldsMenuInv = new HashMap<>();
    public HashMap<Player, Integer> playersPage = new HashMap<>();
    public HashMap<Player, Inventory> playersInv = new HashMap<>();
    public List<ItemStack> presets = new ArrayList<>();


    /**
     * Enumeration representing various actions that can be performed.
     */
    public enum Action {
        Manage,
        Whitelist,
        Banned,
        addWhitelist,
        removeWhitelist,
        Kick,
        Ban,
        Unban
    }
    
    /**
     * Retrieves the GameRulesMenu instance.
     * @return The GameRulesMenu instance.
     */
    public GameRulesMenu getGameRulesMenu() {
        return api.getGameRulesMenu();
    }
    
    /**
     * Retrieves the SettingsMenu instance.
     * @return The SettingsMenu instance.
     */
    public SettingsMenu getSettingsMenu() {
        return api.getSettingsMenu();
    }
    
    /**
     * Retrieves the HousingMenu instance.
     * @return The HousingMenu instance.
     */
    public HousingMenu getHousingMenu() {
        return api.getHousingMenu();
    }
    
    /**
     * Retrieves the OnlinePlayersMenu instance.
     * @return The OnlinePlayersMenu instance.
     */
    public OnlinePlayersMenu getOnlinePlayersMenu() {
        return api.getOnlinePlayersMenu();
    }
    
    /**
     * Retrieves the PlayerManagerMenu instance.
     * @return The PlayerManagerMenu instance.
     */
    public PlayerManagerMenu getPlayerManagerMenu() {
        return api.getPlayerManagerMenu();
    }
    
    /**
     * Retrieves the WhitelistMenu instance.
     * @return The WhitelistMenu instance.
     */
    public WhitelistMenu getWhitelistMenu() {
        return api.getWhitelistMenu();
    }
    
    /**
     * Retrieves the OptionsMenu instance.
     * @return The OptionsMenu instance.
     */
    public OptionsMenu getOptionsMenu() {
        return api.getOptionsMenu();
    }
    
    /**
     * Retrieves the BannedMenu instance.
     * @return The BannedMenu instance.
     */
    public BannedMenu getBannedMenu() {
        return api.getBannedMenu();
    }
    
    /**
     * Retrieves the WorldsMenu instance.
     * @return The WorldsMenu instance.
     */
    public WorldsMenu getWorldsMenu() {
        return api.getWorldsMenu();
    }

    /**
     * Gets the configuration manager.
     * @return The ConfigManager instance.
     */
    public ConfigManager getConfigManager() {
        return api.getConfigManager();
    }
    
    /**
     * Splits a list into sublists of specified size.
     * Can be used for anything, we use it for GUI pages.
     * @param list The list to be split.
     * @param size The size of each sublist.
     * @return A list of sublists.
     */
    public <T> List<List<T>> listToPages(List<T> list, int size) {
        return api.listToPages(list, size);
    }
    
    /**
     * Gets the raw world configuration.
     * Note: Call this method only when necessary.
     * @param uuid The UUID of the world.
     * @return The YamlConfiguration of the world.
     */
    public YamlConfiguration getWorldConfig(UUID uuid) {
        return api.getWorldConfig(uuid);
    }
    
    /**
     * Gets the name of the selected world.
     * @param uuid The UUID of the world.
     * @return The name of the selected world.
     */
    public String getSelectedWorldName(UUID uuid) {
        return api.getSelectedWorldName(uuid);
    }

    /**
     * Adds a world configuration to the map.
     * @param uuid The UUID of the world.
     * @param config The configuration to be added.
     * @return The previous configuration associated with the UUID, if any.
     */
    public YamlConfiguration addWorldConfig(UUID uuid, YamlConfiguration config) {
        return api.addWorldConfig(uuid, config);
	}
	
	/**
	 * Used to get the worlds owner.
	 * @param world The world to check.
     * @return The UUID of the world owner.
     */
	public OfflinePlayer getWorldOwner(World world) {
		return api.getWorldOwner(world);
	}
	
	/**
	 * Used to create custom worlds,
	 * Anything else prints "UnsupportedOperationException"
	 * @param uuid The UUID of the world.
	 * @param worldName The name of the world.
     */
	public void CreateWorld(UUID uuid, String worldName, Boolean isvoid) {
		api.CreateWorld(uuid, worldName, isvoid);
		
	}
	
	/**
	 * Used to create custom worlds,
	 * Anything else prints "UnsupportedOperationException"
	 * @param uuid The UUID of the world.
	 * @param worldName The name of the world.
	 * @param seed The seed of the world.
	 * @param isVoid Use void generator.
	 * @param keepSpawnLoaded Keep spawn loaded in memory.
	 * @param generateStructures Generate structures like villages, ect.
	 * @param environment The world environment.
     */
	public void CreateWorld(UUID uuid, String worldName, int seed, boolean isVoid, boolean keepSpawnLoaded, boolean generateStructures, Environment environment) {
		api.CreateWorld(uuid, worldName, seed, isVoid, keepSpawnLoaded, generateStructures, environment);
	}
	
	/**
	 * Used to unload custom worlds,
	 * Anything else prints "UnsupportedOperationException"
	 * @param uuid The world owners UUID.
	 */
	public void unloadWorld(UUID uuid) {
		api.unloadWorld(uuid);
	}
	
	/**
	 * Gets a players world instance from UUID.
	 * @param uuid The world owners UUID.
     * @return  The world instance.
	 */
	public World getWorld(UUID uuid) {
		return api.getWorld(uuid);
	}
	
	/**
	 * Gets all loaded worlds
	 */
	public HashMap<OfflinePlayer, World> getWorlds() {
		return api.getWorlds();
	}
	
	/*
	 * Joins a players world.
	 * @param player The player to join.
	 * @param uuid The world to join.
	 */
	public void joinWorld(Player player, UUID uuid) {
		api.joinWorld(player, uuid);
	}

	/**
     * Sets the player's world instance. Usually used for changing their worlds.
     * @param offlinePlayer The offline player whose world instance is being set.
     * @param world The world instance to be set.
     */
	public void addWorld(@NotNull OfflinePlayer offlinePlayer, World world) {
		api.addWorld(offlinePlayer, world);
	}
	
	/**
     * Removes the player's world instance. Usually used for changing their worlds.
     * @param offlinePlayer The offline player whose world instance is being set.
     * @param world The world instance to be set.
     */
	public void removeWorld(World world) {
		api.removeWorld(world);
	}
	
	
	/**
     * Retrieves the WorldUtils instance. Deprecated method, use with caution.
     * @return The WorldUtils instance.
     * @deprecated This method is to be replaced by the Bukkit API.
     */
	@Deprecated
	public WorldUtils getWorldUtils() {
		return api.getWorldUtils();
	}
	
	/**
     * Loads a world manually. Not recommended for general use.
     * @param uuid The UUID of the world to be loaded.
     */
	public void loadWorld(UUID uuid) {
		api.loadWorld(uuid);
	}
	
	/**
     * Performs an action on a player.
     * @param source The player performing the action.
     * @param target The target player on which the action is being performed.
     * @param action The action to be performed.
     */
	public void performAction(Player source, OfflinePlayer target, Action action) {
		api.performAction(source, target, action);
	}
	
	/**
     * Retrieves the name of the world from the World object.
     * @param world The world whose name is being retrieved.
     * @return The name of the world.
     */
	public String getWorldNameFromWorld(World world) {
		return api.getWorldNameFromWorld(world);
	}
	
	/**
     * Retrieves the version of the plugin.
     */
	public int[] getPluginVersion() {    	
    	return api.getPluginVersion();
    }
}