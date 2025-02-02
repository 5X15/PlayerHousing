package net.myteria.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import net.myteria.PlayerHousing;

public class ConfigManager {	
    public ConfigManager() {
    	// Unsure if this is still even needed
    	File folder = new File("housing");
    	if (!folder.exists()) {
    		folder.mkdir();
    	}
    }
    
    public boolean hasWorld(UUID uuid) {
    	File file = getFile(uuid);
    	if (file.exists()) {
    		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    		PlayerHousing.getAPI().addWorldConfig(uuid, config);
    	}
    	return file.exists();
    }
    
    public File getFile(UUID uuid) {
    	return new File(String.format("housing/%s/%s.yml", uuid, uuid));
    }
    
    public void setDefaults(UUID uuid, String world) {
    	File file = getFile(uuid);
    	
    	YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    	List<String> whitelist = new ArrayList<String>();
    	whitelist.add(uuid.toString());
    	
    	config.set("default-world", world);
    	config.set("isvoid", true);
    	config.set(world + ".whitelist", whitelist);
    	config.set(world + ".banned", new ArrayList<String>());
    	config.set(world + ".settings.gamemode", "ADVENTURE");
    	config.set(world + ".settings.status", "PRIVATE");
    	config.set(world + ".settings.pvp", false);
    	config.set(world + ".settings.difficulty", "EASY");
    	config.set(world + ".gamerules.blockExplosionDropDecay", false);
    	config.set(world + ".gamerules.doDaylightCycle", false);
    	config.set(world + ".gamerules.doEntityDrops", false);
    	config.set(world + ".gamerules.doFireTick", false);
    	config.set(world + ".gamerules.doInsomnia", false);
    	config.set(world + ".gamerules.doLimitedCrafting", false);
    	config.set(world + ".gamerules.doMobLoot", false);
    	config.set(world + ".gamerules.doMobSpawning", false);
    	config.set(world + ".gamerules.doPatrolSpawning", false);
    	config.set(world + ".gamerules.doTileDrops", false);
    	config.set(world + ".gamerules.doTraderSpawning", false);
    	config.set(world + ".gamerules.doVinesSpread", false);
    	config.set(world + ".gamerules.doWeatherCycle", false);
    	config.set(world + ".gamerules.doWardenSpawning", false);
    	config.set(world + ".gamerules.drowningDamage", false);
    	config.set(world + ".gamerules.enderPearlsVanishOnDeath", false);
    	config.set(world + ".gamerules.fallDamage", false);
    	config.set(world + ".gamerules.fireDamage", false);
    	config.set(world + ".gamerules.forgiveDeadPlayers", false);
    	config.set(world + ".gamerules.freezeDamage", false);
    	config.set(world + ".gamerules.keepInventory", false);
    	config.set(world + ".gamerules.lavaSourceConversion", false);
    	config.set(world + ".gamerules.logAdminCommands", false);
    	config.set(world + ".gamerules.mobExplosionDropDecay", false);
    	config.set(world + ".gamerules.mobGriefing", false);
    	config.set(world + ".gamerules.naturalRegeneration", false);
    	config.set(world + ".gamerules.randomTickSpeed", 3);
    	config.set(world + ".gamerules.reducedDebugInfo", false);
    	config.set(world + ".gamerules.tntExplosionDropDecay", false);
    	try {
			config.save(file);
			PlayerHousing.getAPI().addWorldConfig(uuid, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void verifyConfig(UUID uuid, String world) {
    	YamlConfiguration config;
    	if (PlayerHousing.getAPI().getWorldConfig(uuid) == null) {
    		PlayerHousing.getAPI().addWorldConfig(uuid, YamlConfiguration.loadConfiguration(new File(String.format("housing/%s/%s.yml", uuid, uuid))));
    	}
    	config = PlayerHousing.getAPI().getWorldConfig(uuid);
    	
    	List<String> whitelist = new ArrayList<String>();
    	whitelist.add(uuid.toString());
    	
    	if (config.get("default-world") == null) {
    		config.set("default-world", world);
    	}
    	if (config.get("isVoid") == null) {
    		config.set("isvoid", true);
    	}
    	if (config.getStringList(world + ".whitelist").isEmpty()) {
    		config.set(world + ".whitelist", whitelist);
    	}
    	if (config.getStringList(world + ".banned").isEmpty()) {
    		config.set(world + ".banned", new ArrayList<String>());
    	}
    	if (config.get(world + ".settings.gamemode") == null) {
    		config.set(world + ".settings.gamemode", "ADVENTURE");
    	}
    	if (config.get(world + ".settings.status") == null) {
    		config.set(world + ".settings.status", "PRIVATE");
    	}
    	if (config.get(world + ".settings.pvp") == null) {
    		config.set(world + ".settings.pvp", false);
    	}
    	if (config.get(world + ".settings.difficulty") == null) {
    		config.set(world + ".settings.difficulty", "EASY");
    	}
    	if (config.get(world + ".gamerules.blockExplosionDropDecay") == null) {
    		config.set(world + ".gamerules.blockExplosionDropDecay", false);
    	}
    	if (config.get(world + ".gamerules.doDaylightCycle") == null) {
    		config.set(world + ".gamerules.doDaylightCycle", false);
    	}
    	if (config.get(world + ".gamerules.doEntityDrops") == null) {
    		config.set(world + ".gamerules.doEntityDrops", false);
    	}
    	if (config.get(world + ".gamerules.doFireTick") == null) {
    		config.set(world + ".gamerules.doFireTick", false);
    	}
    	if (config.get(world + ".gamerules.doInsomnia") == null) {
    		config.set(world + ".gamerules.doInsomnia", false);
    	}
    	if (config.get(world + ".gamerules.doLimitedCrafting") == null) {
    		config.set(world + ".gamerules.doLimitedCrafting", false);
    	}
    	if (config.get(world + ".gamerules.doMobLoot") == null) {
    		config.set(world + ".gamerules.doMobLoot", false);
    	}
    	if (config.get(world + ".gamerules.doMobSpawning") == null) {
    		config.set(world + ".gamerules.doMobSpawning", false);
    	}
    	if (config.get(world + ".gamerules.doPatrolSpawning") == null) {
    		config.set(world + ".gamerules.doPatrolSpawning", false);
    	}
    	if (config.get(world + ".gamerules.doTileDrops") == null) {
    		config.set(world + ".gamerules.doTileDrops", false);
    	}
    	if (config.get(world + ".gamerules.doTraderSpawning") == null) {
    		config.set(world + ".gamerules.doTraderSpawning", false);
    	}
    	if (config.get(world + ".gamerules.doVinesSpread") == null) {
    		config.set(world + ".gamerules.doVinesSpread", false);
    	}
    	if (config.get(world + ".gamerules.doWeatherCycle") == null) {
    		config.set(world + ".gamerules.doWeatherCycle", false);
    	}
    	if (config.get(world + ".gamerules.doWardenSpawning") == null) {
    		config.set(world + ".gamerules.doWardenSpawning", false);
    	}
    	if (config.get(world + ".gamerules.drowningDamage") == null) {
    		config.set(world + ".gamerules.drowningDamage", false);
    	}
    	if (config.get(world + ".gamerules.enderPearlsVanishOnDeath") == null) {
    		config.set(world + ".gamerules.enderPearlsVanishOnDeath", false);
    	}
    	if (config.get(world + ".gamerules.fallDamage") == null) {
    		config.set(world + ".gamerules.fallDamage", false);
    	}
    	if (config.get(world + ".gamerules.fireDamage") == null) {
    		config.set(world + ".gamerules.fireDamage", false);
    	}
    	if (config.get(world + ".gamerules.forgiveDeadPlayers") == null) {
    		config.set(world + ".gamerules.forgiveDeadPlayers", false);
    	}
    	if (config.get(world + ".gamerules.freezeDamage") == null) {
    		config.set(world + ".gamerules.freezeDamage", false);
    	}
    	if (config.get(world + ".gamerules.keepInventory") == null) {
    		config.set(world + ".gamerules.keepInventory", false);
    	}
    	if (config.get(world + ".gamerules.lavaSourceConversion") == null) {
    		config.set(world + ".gamerules.lavaSourceConversion", false);
    	}
    	if (config.get(world + ".gamerules.logAdminCommands") == null) {
    		config.set(world + ".gamerules.logAdminCommands", false);
    	}
    	if (config.get(world + ".gamerules.mobExplosionDropDecay") == null) {
    		config.set(world + ".gamerules.mobExplosionDropDecay", false);
    	}
    	if (config.get(world + ".gamerules.mobGriefing") == null) {
    		config.set(world + ".gamerules.mobGriefing", false);
    	}
    	if (config.get(world + ".gamerules.naturalRegeneration") == null) {
    		config.set(world + ".gamerules.naturalRegeneration", false);
    	}
    	if (config.get(world + ".gamerules.randomTickSpeed") == null) {
    		config.set(world + ".gamerules.randomTickSpeed", 3);
    	}
    	if (config.get(world + ".gamerules.reducedDebugInfo") == null) {
    		config.set(world + ".gamerules.reducedDebugInfo", false);
    	}
    	if (config.get(world + ".gamerules.tntExplosionDropDecay") == null) {
    		config.set(world + ".gamerules.tntExplosionDropDecay", false);
    	}
    	
    	try {
			config.save(PlayerHousing.getAPI().getConfigManager().getFile(uuid));
			PlayerHousing.getAPI().addWorldConfig(uuid, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
