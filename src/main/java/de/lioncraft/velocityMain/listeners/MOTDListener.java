package de.lioncraft.velocityMain.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.lioncraft.velocityMain.data.Config;

public class MOTDListener {
    @Subscribe
    public void onRequest(ProxyPingEvent e){
        e.setPing(new ServerPing(e.getPing().getVersion(),
                e.getPing().getPlayers().orElse(null),
                Config.getInstance().getRandomMotdComponent(), 
                e.getPing().getFavicon().orElse(null)));
    }
}
