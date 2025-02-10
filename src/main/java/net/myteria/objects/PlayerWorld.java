package net.myteria.objects;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.myteria.HousingAPI;
import net.myteria.HousingAPI.Status;
import net.myteria.PlayerHousing;
import net.myteria.utils.Scheduler;

public class PlayerWorld {
    private YamlConfiguration config;
    private File file;
    private OfflinePlayer owner;
    private World world;
    private PlayerHousing plugin = PlayerHousing.getInstance();
    
	public PlayerWorld(OfflinePlayer player) {
		this.owner = player;
		this.file = new File("housing", String.format("%s/%s.yml", player.getUniqueId(), player.getUniqueId()));
		this.config = YamlConfiguration.loadConfiguration(this.file);
		this.world = Bukkit.getWorld(String.format("housing/%s/%s", player.getUniqueId(), this.config.getString("default-world", "world")));
	}
	
	public OfflinePlayer getWorldOwner() {
		return this.owner;
	}
	
	public YamlConfiguration getConfig() {
		return this.config;
	}
	
	public boolean save(boolean saveWorld) {
		try {
			this.config.save(this.file);
			if (saveWorld) this.world.save();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public World getWorld() {
		return this.world;
	}

	public String getWorldName() {
		return this.config.getString("default-world", "world");
	}

	public Status getStatus() {
		return Status.valueOf(this.config.getString(getWorldName() + ".settings.status", "PRIVATE"));
	}
	
	public boolean setStatus(Status status) {
		this.config.set(getWorldName() + ".settings.status", status.name().toUpperCase());
		return save(false);
	}
	
	public String getDescription() {
		return this.config.getString(getWorldName() + ".settings.description", "A Player World");
	}
	
	public boolean setDescription(String status) {
		this.config.set(getWorldName() + ".settings.description", (status.length() > 25 ? status.substring(0, 25) : status));
		return save(false);
	}
	
	public boolean setGameRule(GameRule gamerule, Object value) {
		Scheduler.runTask(plugin, () -> {
			this.world.setGameRule(gamerule, value);
		});
		config.set(getWorldName() + ".gamerules." + gamerule.getName(), value);
		return save(false);
	};
	
	public List<String> getBanList() {
		return this.config.getStringList(getWorldName() + ".banned");
	}
	
	public List<String> getWhitelist() {
		return this.config.getStringList(getWorldName() + ".whitelist");
	}
	
	public boolean ban(UUID uuid) {
		if (owner.getUniqueId() == uuid) return false;
		List<String> bans = getBanList();
		if (bans.contains(uuid.toString())) return false;
		bans.add(uuid.toString());
		this.config.set(getWorldName() + ".banned", bans);
		unwhitelist(uuid);
		removePlayer(uuid);
		return save(false);
	}
	
	public boolean unban(UUID uuid) {
		if (owner.getUniqueId() == uuid) return false;
		List<String> bans = getBanList();
		if (!bans.contains(uuid.toString())) return false;
		bans.remove(uuid.toString());
		this.config.set(getWorldName() + ".banned", bans);
		return save(false);
	}
	
	public boolean whitelist(UUID uuid) {
		if (owner.getUniqueId() == uuid) return false;
		List<String> whitelist = getWhitelist();
		if (whitelist.contains(uuid.toString())) return false;
		whitelist.add(uuid.toString());
		this.config.set(getWorldName() + ".whitelist", whitelist);
		return save(false);
	}
	
	public boolean unwhitelist(UUID uuid) {
		if (owner.getUniqueId() == uuid) return false;
		List<String> whitelist = getWhitelist();
		if (!whitelist.contains(uuid.toString())) return false;
		whitelist.remove(uuid.toString());
		this.config.set(getWorldName() + ".whitelist", whitelist);
		return save(false);
	}
	
	public int getPlayersOnline() {
		return this.world.getPlayerCount();
	}
	
	public List<Player> getOnlinePlayers() {
		return this.world.getPlayers();
	}
	
	public Player getOnlinePlayer(UUID uuid) {
		for (Player player : getOnlinePlayers()) {
			if (player.getUniqueId() == uuid) return player;
		}
		return null;
	}
	
	public boolean removePlayer(UUID uuid) {
		if (owner.getUniqueId() == uuid) return false;
		Player player = getOnlinePlayer(uuid);
		if (getOnlinePlayer(uuid) == null) return false;
		if (plugin.isFolia()) {
			player.teleportAsync(Bukkit.getWorld("world").getSpawnLocation().toBlockLocation());
		} else {
			player.teleport(Bukkit.getWorld("world").getSpawnLocation().toBlockLocation());
		}
		return true;
	}
	
	public GameMode getGamemode() {
		return GameMode.valueOf(this.config.getString(getWorldName() + ".settings.gamemode"));
	}
	
	public boolean setGamemode(GameMode gamemode) {
		this.config.set(getWorldName() + ".settings.gamemode", gamemode.name().toUpperCase());
		for (Player player : getOnlinePlayers()) {
			player.setGameMode(gamemode);
		}
		return save(false);
	}
	
	public Difficulty getDifficulty() {
		return this.world.getDifficulty();
	}
	
	public boolean setDifficulty(Difficulty difficulty) {
		Scheduler.runTask(plugin, () -> {
			this.world.setDifficulty(difficulty);
		});
		this.config.set(getWorldName() + ".settings.difficulty", difficulty.name().toUpperCase());
		return save(false);
	}
	
	public boolean getPVP() {
		return this.world.getPVP();
	}
	
	public boolean setPVP(boolean pvp) {
		Scheduler.runTask(plugin, () -> {
			this.world.setPVP(pvp);
		});
		this.config.set(getWorldName() + ".settings.pvp", pvp);
		return save(false);
	}
}
