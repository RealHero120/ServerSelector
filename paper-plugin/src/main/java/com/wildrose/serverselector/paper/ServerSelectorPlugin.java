package com.wildrose.serverselector.paper;

import org.bukkit.plugin.java.JavaPlugin;

public class ServerSelectorPlugin extends JavaPlugin {

    static final String CHANNEL = "serverselector:connect";

    @Override
    public void onEnable() {
        getServer().getMessenger().registerOutgoingPluginChannel(this, CHANNEL);
        getServer().getPluginManager().registerEvents(new CompassListener(), this);
        getServer().getPluginManager().registerEvents(new GuiListener(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this, CHANNEL);
    }
}
