package net.myteria.utils;

import java.io.IOException;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_20_R3.generator.CraftWorldInfo;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom.Dimension;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtException;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.MobSpawnerCat;
import net.minecraft.world.entity.npc.MobSpawnerTrader;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumGamemode;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.MobSpawner;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.WorldDimension;
import net.minecraft.world.level.levelgen.MobSpawnerPatrol;
import net.minecraft.world.level.levelgen.MobSpawnerPhantom;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.Convertable;
import net.minecraft.world.level.storage.LevelDataAndDimensions;
import net.minecraft.world.level.storage.WorldDataServer;
import net.minecraft.world.level.storage.WorldInfo;
import net.minecraft.world.level.validation.ContentValidationException;
import net.myteria.PlayerHousing;

public class WorldUtils {
	public org.bukkit.World createWorld(WorldCreator creator) throws IOException, ContentValidationException {
		org.bukkit.World world = Bukkit.getWorld(creator.name());
	    if (world != null) return world; 
	    
	    ResourceKey<WorldDimension> normalDimension = WorldDimension.b;
	    Convertable.ConversionSession worldSession = Convertable.b(Bukkit.getWorldContainer().toPath()).validateAndCreateAccess(creator.name(), normalDimension);
	    Dynamic<?> dynamic;
	    WorldDataServer worlddata;
	    ResourceKey<World> worldKey;
	    String name = creator.name();
	    ChunkGenerator generator = creator.generator();
	    BiomeProvider biomeProvider = creator.biomeProvider();
	    MinecraftServer server = DedicatedServer.getServer();
	   
	    
	    if (worldSession.k()) {
	        WorldInfo worldinfo;
	        try {
	            dynamic = worldSession.f();
	            worldinfo = worldSession.a(dynamic);
	        } catch (NbtException|net.minecraft.nbt.ReportedNbtException|IOException ioexception) {
	            Convertable.b conversionSession = worldSession.c();
	            Bukkit.getLogger().warning(String.format("Failed to load world data from %s", conversionSession.b()));
	            Bukkit.getLogger().warning("Attempting to use fallback");
	          
	          try {
	              dynamic = worldSession.g();
	              worldinfo = worldSession.a(dynamic);
	          } catch (NbtException|net.minecraft.nbt.ReportedNbtException|IOException ioexception1) {
	        	  Bukkit.getLogger().warning(String.format("Failed to load world data from %s", conversionSession.c()));
	        	  Bukkit.getLogger().warning(String.format("Failed to load world data from %s and %s. World files may be corrupted. Shutting down.", conversionSession.b()));
	              return null;
	          } 
	          worldSession.l();
	        } 
	        if (worldinfo.d()) {
	        	Bukkit.getLogger().warning("This world must be opened in an older version (like 1.6.4) to be safely converted");
	            return null;
	        } 
	        if (!worldinfo.r()) {
	        	Bukkit.getLogger().warning("This world was created by an incompatible version.");
	            return null;
	        } 
	      } else {
	          dynamic = null;
	      } 
	    boolean hardcore = creator.hardcore();
	    WorldLoader.a worldloader = server.worldLoader;
	    IRegistry<WorldDimension> iregistry = worldloader.d().d(Registries.aN);
	    if (dynamic != null) {
	        LevelDataAndDimensions leveldataanddimensions = Convertable.a(dynamic, worldloader.b(), iregistry, worldloader.c());
	        worlddata = (WorldDataServer)leveldataanddimensions.a();
	        iregistry = leveldataanddimensions.b().c();
	    } else {
	        WorldOptions worldoptions = new WorldOptions(creator.seed(), creator.generateStructures(), false);
	        DedicatedServerProperties.WorldDimensionData properties = new DedicatedServerProperties.WorldDimensionData(ChatDeserializer.a(creator.generatorSettings().isEmpty() ? "{}" : creator.generatorSettings()), creator.type().name().toLowerCase(Locale.ROOT));
	        WorldSettings worldsettings = new WorldSettings(name, EnumGamemode.a(GameMode.ADVENTURE.getValue()), hardcore, EnumDifficulty.b, false, new GameRules(), worldloader.b());
	        WorldDimensions worlddimensions = properties.a(worldloader.c());
	        WorldDimensions.b overworld = worlddimensions.a(iregistry);
	        Lifecycle lifecycle = overworld.a().add(worldloader.c().e());
	        worlddata = new WorldDataServer(worldsettings, worldoptions, overworld.d(), lifecycle);
	        iregistry = overworld.c();
	    } 
	    worlddata.customDimensions = iregistry;
	    worlddata.checkName(name);
	    worlddata.a(server.getServerModName(), server.M().a());
	    long seed = BiomeManager.a(creator.seed());
	    ImmutableList<MobSpawner> immutableList = ImmutableList.of(new MobSpawnerPhantom(), new MobSpawnerPatrol(), new MobSpawnerCat(), new VillageSiege(), new MobSpawnerTrader(worlddata));
	    WorldDimension worlddimension = iregistry.a(normalDimension);
	    CraftWorldInfo craftWorldInfo = new CraftWorldInfo(worlddata, worldSession, creator.environment(), worlddimension.a().a(), worlddimension.b(), Dimension.b);
	    if (biomeProvider == null && generator != null) {
	        biomeProvider = generator.getDefaultBiomeProvider(craftWorldInfo); 
		}
	    worldKey = ResourceKey.a(Registries.aM, new MinecraftKey(name.toLowerCase(Locale.ENGLISH)));
	     
	    WorldServer internal = new WorldServer(server, server.av, worldSession, worlddata, worldKey, worlddimension, (server).I.create(11), 
	        worlddata.C(), seed, (creator.environment() == org.bukkit.World.Environment.NORMAL) ? immutableList : ImmutableList.of(), true, server.F().J(), creator.environment(), generator, biomeProvider);
	    ChunkProviderServer chunkProviderService = internal.l();
	    internal.keepSpawnInMemory = creator.keepSpawnLoaded().toBoolean();

	    if (internal.randomSpawnSelection == null)internal.randomSpawnSelection = new ChunkCoordIntPair(internal.l().i().b().a());
	    Bukkit.getRegionScheduler().execute(PlayerHousing.getInstance(), internal.getWorld().getSpawnLocation(), () -> {
	    	if (internal.getWorld().getSpawnLocation().subtract(0, 1, 0).getBlock().getType() == Material.AIR) {
	    		internal.getWorld().getSpawnLocation().subtract(0, 1, 0).getBlock().setType(Material.STONE);
	    	}
	    	DedicatedServer.getServer().initWorld(internal, worlddata, worlddata, worlddata.A());
        });
	    
	    internal.b(true, true);
	    server.addLevel(internal);
	    server.prepareLevels((chunkProviderService).a.D, internal);
	    Bukkit.getServer().getPluginManager().callEvent(new WorldLoadEvent(internal.getWorld()));
	    return internal.getWorld();
	  }
}
