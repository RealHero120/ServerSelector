package com.wildrose.serverselector.paper;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Marks inventories created by this plugin so click events can be identified
 * without fragile title comparisons.
 */
public class SelectorHolder implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return null;
    }
}
