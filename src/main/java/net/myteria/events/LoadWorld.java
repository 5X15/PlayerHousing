package net.myteria.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import net.myteria.HousingAPI;
import net.myteria.PlayerHousing;

public class LoadWorld implements Listener{

	public void onWorldLoad(WorldLoadEvent event) {
		if (event.getWorld().getName().contains("/")) {
			HousingAPI api = PlayerHousing.getAPI();
			World world = event.getWorld();
			String name = event.getWorld().getName().split("/")[2];
			UUID uuid = api.getWorldOwner(world).getUniqueId();
			YamlConfiguration config = api.getWorldConfig(uuid);
			
			setGameRules(world, name, config);
			
			world.setPVP(config.getBoolean(name + ".settings.pvp"));
			world.setDifficulty(Difficulty.valueOf(config.getString(name + ".settings.difficulty")));
		}
		
	}
	
	private void setGameRules(World world, String name, YamlConfiguration config) {		
		
		world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		world.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, config.getBoolean(name + ".gamerules.blockExplosionDropDecay"));
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, config.getBoolean(name + ".gamerules.doDaylightCycle"));
		world.setGameRule(GameRule.DO_ENTITY_DROPS, config.getBoolean(name + ".gamerules.doEntityDrops"));
		world.setGameRule(GameRule.DO_FIRE_TICK, config.getBoolean(name + ".gamerules.doFireTick"));
		world.setGameRule(GameRule.DO_INSOMNIA, config.getBoolean(name + ".gamerules.doInsomnia"));
		world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, false);
		world.setGameRule(GameRule.DO_LIMITED_CRAFTING, config.getBoolean(name + ".gamerules.doLimitedCrafting"));
		world.setGameRule(GameRule.DO_MOB_LOOT, config.getBoolean(name + ".gamerules.doMobLoot"));
		world.setGameRule(GameRule.DO_MOB_SPAWNING, config.getBoolean(name + ".gamerules.doMobSpawning"));
		world.setGameRule(GameRule.DO_PATROL_SPAWNING, config.getBoolean(name + ".gamerules.doPatrolSpawning"));
		world.setGameRule(GameRule.DO_TILE_DROPS, config.getBoolean(name + ".gamerules.doTileDrops"));
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, config.getBoolean(name + ".gamerules.doTraderSpawning"));
		world.setGameRule(GameRule.DO_VINES_SPREAD, config.getBoolean(name + ".gamerules.doVinesSpread"));
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, config.getBoolean(name + ".gamerules.doWeatherCycle"));
		world.setGameRule(GameRule.DO_WARDEN_SPAWNING, config.getBoolean(name + ".gamerules.doWardenSpawning"));
		world.setGameRule(GameRule.DROWNING_DAMAGE, config.getBoolean(name + ".gamerules.drowningDamage"));
		world.setGameRule(GameRule.ENDER_PEARLS_VANISH_ON_DEATH, config.getBoolean(name + ".gamerules.enderPearlsVanishOnDeath"));
		world.setGameRule(GameRule.FALL_DAMAGE, config.getBoolean(name + ".gamerules.fallDamage"));
		world.setGameRule(GameRule.FIRE_DAMAGE, config.getBoolean(name + ".gamerules.fireDamage"));
		world.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, config.getBoolean(name + ".gamerules.forgiveDeadPlayers"));
		world.setGameRule(GameRule.FREEZE_DAMAGE, config.getBoolean(name + ".gamerules.freezeDamage"));
		world.setGameRule(GameRule.KEEP_INVENTORY, config.getBoolean(name + ".gamerules.keepInventory"));
		world.setGameRule(GameRule.LAVA_SOURCE_CONVERSION, config.getBoolean(name + ".gamerules.lavaSourceConversion"));
		world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, config.getBoolean(name + ".gamerules.logAdminCommands"));
		world.setGameRule(GameRule.MOB_EXPLOSION_DROP_DECAY, config.getBoolean(name + ".gamerules.mobExplosionDropDecay"));
		world.setGameRule(GameRule.NATURAL_REGENERATION, config.getBoolean(name + ".gamerules.naturalRegeneration"));
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, config.getInt(name + ".gamerules.randomTickSpeed"));
		world.setGameRule(GameRule.REDUCED_DEBUG_INFO, config.getBoolean(name + ".gamerules.reducedDebugInfo"));
		world.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, config.getBoolean(name + ".gamerules.tntExplosionDropDecay"));

	}
}
