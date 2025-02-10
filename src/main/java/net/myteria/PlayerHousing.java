package net.myteria;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import net.myteria.commands.housingCmd;
import net.myteria.commands.visitCmd;
import net.myteria.events.*;
import net.myteria.menus.*;

public final class PlayerHousing extends JavaPlugin {
	private static HousingAPI api;
	private static PlayerHousing instance;

	public GameRulesMenu gameRulesMenu;
	public SettingsMenu settingsMenu;
	public HousingMenu housingMenu;
	public WhitelistMenu whitelistMenu;
	public OnlinePlayersMenu playersMenu;
	public PlayerManagerMenu managerMenu;
	public OptionsMenu optionsMenu;
	public BannedMenu bannedMenu;
	public TemplatesMenu templatesMenu;
	
	public void onEnable() {
		instance = this;
		api = new HousingAPI(this);
		setupConfig();
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
		gameRulesMenu = new GameRulesMenu();
		settingsMenu = new SettingsMenu();
		housingMenu = new HousingMenu();
		managerMenu = new PlayerManagerMenu();
		playersMenu = new OnlinePlayersMenu();
		whitelistMenu = new WhitelistMenu();
		bannedMenu = new BannedMenu();
		optionsMenu = new OptionsMenu();
		templatesMenu = new TemplatesMenu();
	}
	
	public static HousingAPI getAPI() {
		return api;
	}
	
	public static PlayerHousing getInstance() {
		return instance;
	}
	
	private final void setupConfig() {
		if (!this.getConfig().isSet("worlds")) {
        	this.saveDefaultConfig();
        }
        
        for (String world: this.getConfig().getConfigurationSection("worlds").getKeys(false)) {
        	ItemStack item = new ItemStack(Material.getMaterial(this.getConfig().getString("worlds." + world + ".icon", "stone")));
        	ItemMeta meta = item.getItemMeta();
        	meta.setDisplayName(this.getConfig().getString("worlds." + world + ".name", "Unknown Template"));
        	meta.setLore(this.getConfig().getStringList("worlds." + world + ".description"));
        	meta.getPersistentDataContainer().set(new NamespacedKey(this, "isVoid"), PersistentDataType.BOOLEAN, this.getConfig().getBoolean("worlds." + world + ".isVoid", true));	
        	meta.getPersistentDataContainer().set(new NamespacedKey(this, "folder"), PersistentDataType.STRING, this.getConfig().getString("worlds." + world + ".folder", null));	
            item.setItemMeta(meta);
        	api.presets.add(item);
        }
	}
	
	public boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
