package site.besd.customWorldGenerator.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import site.besd.customWorldGenerator.CustomWorldGenerator;
import site.besd.customWorldGenerator.managers.WorldManager;
import site.besd.customWorldGenerator.types.BiomeType;

import java.util.Arrays;
import java.util.List;

public class WorldSelectionGUI implements Listener {
    
    private final CustomWorldGenerator plugin;
    private final WorldManager worldManager;
    
    public WorldSelectionGUI(CustomWorldGenerator plugin, WorldManager worldManager) {
        this.plugin = plugin;
        this.worldManager = worldManager;
    }
    
    public void openWorldSelectionMenu(Player player) {
        // create the main selection GUI
        Inventory gui = Bukkit.createInventory(null, 27, "§6§lCreate World - Choose Biome");

        // Plains
        ItemStack plains = createMenuItem(Material.GRASS_BLOCK, 
            "§aPlains World", 
            Arrays.asList(
                "§7A peaceful world with grass surface",
                "§e► Grass block surface",
                "§e► Green and peaceful"
            ));
        gui.setItem(10, plains);
        
        // Desert
        ItemStack desert = createMenuItem(Material.SAND, 
            "§eClean Desert World", 
            Arrays.asList(
                "§7A dry world with sand surface",
                "§e► Sand surface",
                "§e► Desert biome"
            ));
        gui.setItem(12, desert);
        
        // Red Sand
        ItemStack redSand = createMenuItem(Material.RED_SAND, 
            "§cRed Sand World", 
            Arrays.asList(
                "§7A mesa world with red sand surface",
                "§e► Red sand surface",
                "§e► Badlands biome"
            ));
        gui.setItem(14, redSand);
        
        // Mushroom
        ItemStack mushroom = createMenuItem(Material.RED_MUSHROOM_BLOCK, 
            "§dMushroom World", 
            Arrays.asList(
                "§7A mystical world with mycelium surface",
                "§e► Mycelium surface",
                "§e► No monsters"
            ));
        gui.setItem(16, mushroom);

        
        // Close button
        ItemStack close = createMenuItem(Material.BARRIER, 
            "§cClose Menu", 
            Arrays.asList("§7Click to close"));
        gui.setItem(22, close);
        
        player.openInventory(gui);
    }
    
    private ItemStack createMenuItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("Create World")) return;
        
        event.setCancelled(true);
        
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        Material type = clickedItem.getType();
        player.closeInventory();

        switch (type) {
            case GRASS_BLOCK:
                worldManager.createWorld(player, BiomeType.PLAINS);
                break;
            case SAND:
                worldManager.createWorld(player, BiomeType.DESERT);
                break;
            case RED_SAND:
                worldManager.createWorld(player, BiomeType.RED_SAND);
                break;
            case RED_MUSHROOM_BLOCK:
                worldManager.createWorld(player, BiomeType.MUSHROOM);
                break;
            case BARRIER:
                player.sendMessage("§aMenu closed!");
                break;
        }
    }
}