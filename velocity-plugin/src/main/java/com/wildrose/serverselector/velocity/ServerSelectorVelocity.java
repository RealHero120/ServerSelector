package com.wildrose.serverselector.velocity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;

@Plugin(
        id = "serverselector",
        name = "ServerSelector",
        version = "1.0.0",
        description = "Server selector for Wild Rose MC"
)
public class ServerSelectorVelocity {

    private static final MinecraftChannelIdentifier CHANNEL =
            MinecraftChannelIdentifier.from("serverselector:connect");

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public ServerSelectorVelocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(CHANNEL);
        logger.info("ServerSelector enabled.");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().equals(CHANNEL)) return;
        if (!(event.getSource() instanceof ServerConnection connection)) return;

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String serverName = in.readUTF();

        Player player = connection.getPlayer();
        server.getServer(serverName).ifPresentOrElse(
                registeredServer -> player.createConnectionRequest(registeredServer).fireAndForget(),
                () -> logger.warn("ServerSelector: unknown server '{}' requested by {}", serverName, player.getUsername()));
    }
}
