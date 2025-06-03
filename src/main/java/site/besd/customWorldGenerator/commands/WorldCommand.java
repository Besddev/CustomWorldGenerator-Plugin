package site.besd.customWorldGenerator.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import site.besd.customWorldGenerator.gui.WorldSelectionGUI;

public class WorldCommand implements CommandExecutor {
    
    private final WorldSelectionGUI worldGUI;
    
    public WorldCommand(WorldSelectionGUI worldGUI) {
        this.worldGUI = worldGUI;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (command.getName().equalsIgnoreCase("createworld")) {
            // open da world selection gui
            worldGUI.openWorldSelectionMenu(player);
            return true;
        }
        
        return false;
    }
}