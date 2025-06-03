package site.besd.customWorldGenerator.generators;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.bukkit.util.noise.SimplexOctaveGenerator;
import org.jetbrains.annotations.NotNull;
import site.besd.customWorldGenerator.types.BiomeType;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {
    
    private final Biome targetBiome;
    private final BiomeType biomeType;
    int currentHeight = 50;

    public CustomChunkGenerator(Biome targetBiome, BiomeType biomeType) {
        this.targetBiome = targetBiome;
        this.biomeType = biomeType;
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new SingleBiomeProvider(targetBiome);
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        ChunkData chunk = createChunkData(world);
        generator.setScale(0.005D);

        for (int X = 0; X < 16; X++) {
            for (int Z = 0; Z < 16; Z++) {
                currentHeight = (int) (generator.noise(chunkX*16+X, chunkZ*16+Z, 0.5D, 0.5D)*15D+50D);
                
                // Set surface block based on biome type
                Material surfaceMaterial = getSurfaceMaterial(biomeType);
                chunk.setBlock(X, currentHeight, Z, surfaceMaterial);
                
                // Set subsurface block (dirt for grass surfaces, sand for sand surfaces, etc.)
                Material subsurfaceMaterial = getSubsurfaceMaterial(biomeType);
                chunk.setBlock(X, currentHeight-1, Z, subsurfaceMaterial);
                
                // Fill with stone below subsurface
                for (int i = currentHeight-2; i > 0; i--) {
                    chunk.setBlock(X, i, Z, Material.STONE);
                }
                
                // Bedrock at bottom
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        }
        return chunk;
    }
    
    private Material getSurfaceMaterial(BiomeType biomeType) {
        switch (biomeType) {
            case PLAINS:
                return Material.GRASS_BLOCK;
            case DESERT:
                return Material.SAND;
            case RED_SAND:
                return Material.RED_SAND;
            case MUSHROOM:
                return Material.MYCELIUM;
            default:
                return Material.GRASS_BLOCK;
        }
    }
    
    private Material getSubsurfaceMaterial(BiomeType biomeType) {
        switch (biomeType) {
            case PLAINS:
                return Material.DIRT;
            case DESERT:
                return Material.SAND;
            case RED_SAND:
                return Material.RED_SAND;
            case MUSHROOM:
                return Material.DIRT;
            default:
                return Material.DIRT;
        }
    }

    @Override
    public boolean shouldGenerateMobs() {
        return false; // No mobs spawning
    }
    
    @Override
    public boolean shouldGenerateStructures() {
        return false; // No structures
    }

    // Custom BiomeProvider to force single biome
    private static class SingleBiomeProvider extends BiomeProvider {
        
        private final Biome biome;
        
        public SingleBiomeProvider(Biome biome) {
            this.biome = biome;
        }
        
        @Override
        public @NotNull Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
            return biome;
        }

        @Override
        public @NotNull List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
            return Collections.singletonList(biome);
        }
    }
}