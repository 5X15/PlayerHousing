package net.myteria.utils;

import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import io.papermc.paper.datapack.PaperDatapack;
import net.minecraft.server.packs.repository.ResourcePackLoader;

public class voidWorld extends ChunkGenerator {
	@Override
    @Nonnull
    public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biome) {
		ChunkData chunk = Bukkit.getServer().createChunkData(world);
		
        return chunk;
    }

}
