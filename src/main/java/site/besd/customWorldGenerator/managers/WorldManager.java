package site.besd.customWorldGenerator.managers;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import site.besd.customWorldGenerator.CustomWorldGenerator;
import site.besd.customWorldGenerator.generators.CustomChunkGenerator;
import site.besd.customWorldGenerator.types.BiomeType;

public class WorldManager {
    
    private final CustomWorldGenerator plugin;
    
    public WorldManager(CustomWorldGenerator plugin) {
        this.plugin = plugin;
    }
    
    public void createWorld(Player player, BiomeType biomeType) {
        String worldName = biomeType.getDisplayName().toLowerCase();
        
        player.sendMessage("§aCreating your " + biomeType.getDisplayName() + " world...");
        
        // Create world synchronously on main thread
        Bukkit.getScheduler().runTask(plugin, () -> {
            try {
                WorldCreator creator = createWorldCreator(worldName, biomeType);
                World world = creator.createWorld();
                
                if (world != null) {
                    world.setSpawnFlags(false, false); // No mobs n animals
                    
                    player.teleport(world.getSpawnLocation());
                    
                    player.sendMessage("§aWorld successfully created!");
                    player.sendMessage("§eWelcome to your new " + biomeType.getDisplayName() + "!");
                    player.sendMessage("§7World generated with natural terrain and " + getSurfaceDescription(biomeType));
                } else {
                    player.sendMessage("§cError creating world!");
                }
            } catch (Exception e) {
                plugin.getLogger().severe("Error creating world: " + e.getMessage());
                player.sendMessage("§cAn error occurred: " + e.getMessage());
            }
        });
    }
    
    private WorldCreator createWorldCreator(String worldName, BiomeType biomeType) {
        WorldCreator creator = new WorldCreator(worldName);
        creator.type(WorldType.NORMAL);
        
        // Get the corresponding biome
        Biome targetBiome = getBiomeFromType(biomeType);
        
        // Set our custom noise-based generator with biome type
        creator.generator(new CustomChunkGenerator(targetBiome, biomeType));
        
        return creator;
    }
    
    private Biome getBiomeFromType(BiomeType biomeType) {
        switch (biomeType) {
            case PLAINS:
                return Biome.PLAINS;
            case DESERT:
                return Biome.DESERT;
            case MUSHROOM:
                return Biome.MUSHROOM_FIELDS;
            case RED_SAND:
                return Biome.BADLANDS;
            default:
                return Biome.PLAINS;
        }
    }
    
    private String getSurfaceDescription(BiomeType biomeType) {
        switch (biomeType) {
            case PLAINS:
                return "grass surface";
            case DESERT:
                return "sand surface";
            case RED_SAND:
                return "red sand surface";
            case MUSHROOM:
                return "mycelium surface";
            default:
                return "custom surface";
        }
    }
    
    public boolean worldExists(String worldName) {
        return Bukkit.getWorld(worldName) != null;
    }
    
    public void deleteWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            // Kick all players from the world
            for (Player player : world.getPlayers()) {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
            
            Bukkit.unloadWorld(world, false);
            plugin.getLogger().info("World " + worldName + " has been unloaded.");
        }
    }
}