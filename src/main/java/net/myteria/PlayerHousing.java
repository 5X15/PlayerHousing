package net.myteria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.myteria.commands.housingCmd;
import net.myteria.commands.visitCmd;
import net.myteria.events.BannedEvent;
import net.myteria.events.ConfirmEvent;
import net.myteria.events.GameRulesEvent;
import net.myteria.events.HousingEvent;
import net.myteria.events.JoinEvent;
import net.myteria.events.LeaveEvent;
import net.myteria.events.OnlinePlayersEvent;
import net.myteria.events.OptionsEvent;
import net.myteria.events.PermissionsEvent;
import net.myteria.events.PlayerManagerEvent;
import net.myteria.events.SettingsEvent;
import net.myteria.events.WhitelistEvent;
import net.myteria.events.WorldsMenuEvent;
import net.myteria.menus.BannedMenu;
import net.myteria.menus.GameRulesMenu;
import net.myteria.menus.HousingMenu;
import net.myteria.menus.OnlinePlayersMenu;
import net.myteria.menus.OptionsMenu;
import net.myteria.menus.PermissionsMenu;
import net.myteria.menus.PlayerManagerMenu;
import net.myteria.menus.SettingsMenu;
import net.myteria.menus.WhitelistMenu;
import net.myteria.menus.WorldsMenu;

public final class PlayerHousing extends JavaPlugin {
	private static HousingAPI api;
	public static PlayerHousing instance;

	public GameRulesMenu gameRulesMenu;
	public SettingsMenu settingsMenu;
	public HousingMenu housingMenu;
	public WhitelistMenu whitelistMenu;
	public OnlinePlayersMenu playersMenu;
	public PlayerManagerMenu managerMenu;
	public OptionsMenu optionsMenu;
	public BannedMenu bannedMenu;
	public WorldsMenu worldsMenu;
	public PermissionsMenu permissionsMenu;
	
	public void onEnable() {
		try {
			Class.forName("net.luckperms.api.LuckPermsProvider");
		} catch (ClassNotFoundException e) {
			Bukkit.getLogger().warning("[PlayerHousing] LuckPerms not installed, disabling plugin.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		instance = this;
		api = new HousingAPI();
		setupConfigs();
		getCommand("visit").setExecutor(new visitCmd());
		getCommand("housing").setExecutor(new housingCmd());
		getServer().getPluginManager().registerEvents(new JoinEvent(), this);
		getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
		getServer().getPluginManager().registerEvents(new GameRulesEvent(), this);
		getServer().getPluginManager().registerEvents(new SettingsEvent(), this);
		getServer().getPluginManager().registerEvents(new HousingEvent(), this);
		getServer().getPluginManager().registerEvents(new PlayerManagerEvent(), this);
		getServer().getPluginManager().registerEvents(new OnlinePlayersEvent(), this);
		getServer().getPluginManager().registerEvents(new ConfirmEvent(), this);
		getServer().getPluginManager().registerEvents(new WhitelistEvent(), this);
		getServer().getPluginManager().registerEvents(new OptionsEvent(), this);
		getServer().getPluginManager().registerEvents(new BannedEvent(), this);
		getServer().getPluginManager().registerEvents(new WorldsMenuEvent(), this);
		getServer().getPluginManager().registerEvents(new PermissionsEvent(), this);
		gameRulesMenu = new GameRulesMenu();
		settingsMenu = new SettingsMenu();
		housingMenu = new HousingMenu();
		managerMenu = new PlayerManagerMenu();
		playersMenu = new OnlinePlayersMenu();
		whitelistMenu = new WhitelistMenu();
		bannedMenu = new BannedMenu();
		optionsMenu = new OptionsMenu();
		worldsMenu = new WorldsMenu();
		permissionsMenu = new PermissionsMenu();
		
	}
	
	public static HousingAPI getAPI() {
		return api;
	}
	
	public static PlayerHousing getInstance() {
		return instance;
	}
	
	private final void setupConfigs() {
		Path directory = Paths.get("housing");
		if (!this.getConfig().isSet("worlds") || !this.getConfig().isSet("permissions")) {
        	this.saveDefaultConfig();
        }
        try {
            List<Path> configs = Files.walk(directory, 2)  // Limit to one level deep
                .filter(path -> path.toFile().isFile() && path.toString().endsWith(".yml"))
                .collect(Collectors.toList());

            for (Path config : configs) {
                api.worldConfigs.put(UUID.fromString(config.getFileName().toString().replace(".yml", "")), YamlConfiguration.loadConfiguration(config.toFile()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        api.permissionNodes = (List<String>) this.getConfig().getList("permissions");
        
        for (String world: this.getConfig().getConfigurationSection("worlds").getKeys(false)) {
        	ItemStack item = new ItemStack(Material.getMaterial(this.getConfig().getString("worlds." + world + ".icon")));
        	ItemMeta meta = item.getItemMeta();
        	meta.setDisplayName(this.getConfig().getString("worlds." + world + ".name"));
        	meta.setLore((List<String>)this.getConfig().getList("worlds." + world + ".description"));
        	meta.getPersistentDataContainer().set(new NamespacedKey(this, "folder"), PersistentDataType.STRING, this.getConfig().getString("worlds." + world + ".folder"));	
            item.setItemMeta(meta);
        	api.presets.add(item);
        }
	}
}
