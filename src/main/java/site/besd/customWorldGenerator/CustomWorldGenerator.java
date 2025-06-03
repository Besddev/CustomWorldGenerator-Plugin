package site.besd.customWorldGenerator;

import org.bukkit.plugin.java.JavaPlugin;
import site.besd.customWorldGenerator.commands.WorldCommand;
import site.besd.customWorldGenerator.generators.CustomChunkGenerator;
import site.besd.customWorldGenerator.gui.WorldSelectionGUI;
import site.besd.customWorldGenerator.managers.WorldManager;

public class CustomWorldGenerator extends JavaPlugin {
    
    private WorldManager worldManager;
    private WorldSelectionGUI worldGUI;
    
    @Override
    public void onEnable() {
        getLogger().info("CustomWorldGenerator is starting up!");
        
        // managers
        this.worldManager = new WorldManager(this);
        this.worldGUI = new WorldSelectionGUI(this, worldManager);
        
        // commands
        getCommand("createworld").setExecutor(new WorldCommand(worldGUI));
        
        // events
        getServer().getPluginManager().registerEvents(worldGUI, this);
        
        getLogger().info("World generator is ready to use!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("CustomWorldGenerator has been disabled! Auf Wiedersehen! (Goodbye!)");
    }
    
    public WorldManager getWorldManager() {
        return worldManager;
    }
    
    public WorldSelectionGUI getWorldGUI() {
        return worldGUI;
    }

}