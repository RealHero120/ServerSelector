package com.wildrose.serverselector.paper;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CompassListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack slot0 = player.getInventory().getItem(0);
        if (slot0 == null || slot0.getType() != Material.COMPASS) {
            player.getInventory().setItem(0, buildCompass());
        }
    }

    static ItemStack buildCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();
        meta.setDisplayName("§6Server Selector");
        compass.setItemMeta(meta);
        return compass;
    }
}
