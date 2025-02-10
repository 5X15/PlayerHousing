package net.myteria.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import net.myteria.PlayerHousing;
import net.myteria.objects.PlayerWorld;

public class ConfigManager {	
    public ConfigManager() {
    	// Unsure if this is still even needed
    	File folder = new File("housing");
    	if (!folder.exists()) {
    		folder.mkdir();
    	}
    }
    
    public boolean hasWorld(UUID uuid) {
    	return getFile(uuid).exists();
    }
    
    public File getFile(UUID uuid) {
    	return new File(String.format("housing/%s/%s.yml", uuid, uuid));
    }
    
    public void verifyConfig(UUID uuid) {
    	boolean save = false;
    	YamlConfiguration config;
    	if (PlayerHousing.getAPI().getWorldInstance(uuid) == null) {
    		PlayerHousing.getAPI().addWorldInstance(Bukkit.getOfflinePlayer(uuid));
    	}
    	config = PlayerHousing.getAPI().getWorldInstance(uuid).getConfig();
    	
    	List<String> whitelist = new ArrayList<String>();
    	whitelist.add(uuid.toString());
    	
    	if (config.get("default-world") == null) {
    		config.set("default-world", "world");
    		save = true;
    	}
    	String world = config.getString("default-world");
    	
    	if (config.get("isVoid") == null) {
    		config.set("isvoid", true);
    		save = true;
    	}
    	if (config.getStringList(world + ".whitelist").isEmpty()) {
    		config.set(world + ".whitelist", whitelist);
    		save = true;
    	}
    	if (config.getStringList(world + ".banned").isEmpty()) {
    		config.set(world + ".banned", new ArrayList<String>());
    		save = true;
    	}
    	if (config.get(world + ".settings.gamemode") == null) {
    		config.set(world + ".settings.gamemode", "ADVENTURE");
    		save = true;
    	}
    	if (config.get(world + ".settings.status") == null) {
    		config.set(world + ".settings.status", "PRIVATE");
    		save = true;
    	}
    	if (config.get(world + ".settings.description") == null) {
    		config.set(world + ".settings.description", "A Player World");
    		save = true;
    	}
    	if (config.get(world + ".settings.pvp") == null) {
    		config.set(world + ".settings.pvp", false);
    		save = true;
    	}
    	if (config.get(world + ".settings.difficulty") == null) {
    		config.set(world + ".settings.difficulty", "EASY");
    		save = true;
    	}
    	if (config.get(world + ".gamerules.blockExplosionDropDecay") == null) {
    		config.set(world + ".gamerules.blockExplosionDropDecay", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doDaylightCycle") == null) {
    		config.set(world + ".gamerules.doDaylightCycle", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doEntityDrops") == null) {
    		config.set(world + ".gamerules.doEntityDrops", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doFireTick") == null) {
    		config.set(world + ".gamerules.doFireTick", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doInsomnia") == null) {
    		config.set(world + ".gamerules.doInsomnia", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doLimitedCrafting") == null) {
    		config.set(world + ".gamerules.doLimitedCrafting", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doMobLoot") == null) {
    		config.set(world + ".gamerules.doMobLoot", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doMobSpawning") == null) {
    		config.set(world + ".gamerules.doMobSpawning", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doPatrolSpawning") == null) {
    		config.set(world + ".gamerules.doPatrolSpawning", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doTileDrops") == null) {
    		config.set(world + ".gamerules.doTileDrops", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doTraderSpawning") == null) {
    		config.set(world + ".gamerules.doTraderSpawning", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doVinesSpread") == null) {
    		config.set(world + ".gamerules.doVinesSpread", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doWeatherCycle") == null) {
    		config.set(world + ".gamerules.doWeatherCycle", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.doWardenSpawning") == null) {
    		config.set(world + ".gamerules.doWardenSpawning", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.drowningDamage") == null) {
    		config.set(world + ".gamerules.drowningDamage", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.enderPearlsVanishOnDeath") == null) {
    		config.set(world + ".gamerules.enderPearlsVanishOnDeath", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.fallDamage") == null) {
    		config.set(world + ".gamerules.fallDamage", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.fireDamage") == null) {
    		config.set(world + ".gamerules.fireDamage", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.forgiveDeadPlayers") == null) {
    		config.set(world + ".gamerules.forgiveDeadPlayers", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.freezeDamage") == null) {
    		config.set(world + ".gamerules.freezeDamage", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.keepInventory") == null) {
    		config.set(world + ".gamerules.keepInventory", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.lavaSourceConversion") == null) {
    		config.set(world + ".gamerules.lavaSourceConversion", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.logAdminCommands") == null) {
    		config.set(world + ".gamerules.logAdminCommands", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.mobExplosionDropDecay") == null) {
    		config.set(world + ".gamerules.mobExplosionDropDecay", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.mobGriefing") == null) {
    		config.set(world + ".gamerules.mobGriefing", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.naturalRegeneration") == null) {
    		config.set(world + ".gamerules.naturalRegeneration", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.randomTickSpeed") == null) {
    		config.set(world + ".gamerules.randomTickSpeed", 3);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.reducedDebugInfo") == null) {
    		config.set(world + ".gamerules.reducedDebugInfo", false);
    		save = true;
    	}
    	if (config.get(world + ".gamerules.tntExplosionDropDecay") == null) {
    		config.set(world + ".gamerules.tntExplosionDropDecay", false);
    		save = true;
    	}
    	if (save) {
    		PlayerHousing.getAPI().getWorldInstance(uuid).save(false);
    	}
    }
}
