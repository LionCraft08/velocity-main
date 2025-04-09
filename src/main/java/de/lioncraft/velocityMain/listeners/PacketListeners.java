package de.lioncraft.velocityMain.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.util.ServerLink;
import net.kyori.adventure.text.Component;

import java.util.List;

public class PacketListeners {
    @Subscribe
    public void onServerLinkSend(ServerPostConnectEvent e){
        e.getPlayer().setServerLinks(List.of(
                ServerLink.serverLink(Component.text("Regelwerk"), "https://server.castcrafter.de/rules.html"),
                ServerLink.serverLink(Component.text("Discord"), "https://discord.gg/castcrafter"),
                ServerLink.serverLink(Component.text("Event-Informationen"), "https://server.castcrafter.de/event-server.html"),
                ServerLink.serverLink(Component.text("Ticket erstellen (Bugreport, Unban, etc.)"), "https://discord.com/channels/133198459531558912/1297295229870800907"),
                ServerLink.serverLink(Component.text("Updates"), "https://discord.com/channels/133198459531558912/980810495877607524"),
                ServerLink.serverLink(Component.text("FAQ"), "https://server.castcrafter.de/faq.html")
        ));
    }
}
