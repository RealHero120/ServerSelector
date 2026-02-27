package com.wildrose.serverselector.paper;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiListener implements Listener {

    private final ServerSelectorPlugin plugin;

    public GuiListener(ServerSelectorPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getType() != Material.COMPASS) return;
        if (!event.getAction().isRightClick()) return;

        event.setCancelled(true);
        openSelector((Player) event.getPlayer());
    }

    private void openSelector(Player player) {
        Inventory inv = Bukkit.createInventory(new SelectorHolder(), 9, "Server Selector");
        inv.setItem(2, buildItem(Material.GRASS_BLOCK, "§aSMP"));
        inv.setItem(4, buildItem(Material.BOW, "§bMinigames"));
        inv.setItem(6, buildItem(Material.NETHER_STAR, "§eLobby"));
        player.openInventory(inv);
    }

    private static ItemStack buildItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;
        if (!(event.getClickedInventory().getHolder() instanceof SelectorHolder)) return;

        event.setCancelled(true);
        player.closeInventory();

        String server = switch (event.getSlot()) {
            case 2 -> "smp";
            case 4 -> "minigames";
            case 6 -> "lobby";
            default -> null;
        };

        if (server != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(server);
            player.sendPluginMessage(plugin, ServerSelectorPlugin.CHANNEL, out.toByteArray());
        }
    }
}
