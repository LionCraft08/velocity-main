package de.lioncraft.velocityMain.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.*;
import com.velocitypowered.api.proxy.Player;
import de.lioncraft.velocityMain.VelocityMain;
import de.lioncraft.velocityMain.messageHandling.MessageSender;
import de.lioncraft.velocityMain.utils.GUIElementRenderer;

import java.time.ZoneId;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PlayerListeners {
    @Subscribe
    public void onJoin(ServerPostConnectEvent e){
        Player p = e.getPlayer();
        MessageSender.sendHeader(p);
        MessageSender.sendFooter(p);
        VelocityMain.getMain().getServer().getScheduler().buildTask(VelocityMain.getMain(), () -> {
            MessageSender.sendFooter(p);
            MessageSender.sendHeader(p);
        }).delay(2, TimeUnit.SECONDS).schedule();
    }
    @Subscribe
    public void onDC(DisconnectEvent e){
        for(Player p : VelocityMain.getMain().getServer().getAllPlayers()){
            if (p != e.getPlayer()) MessageSender.sendFooter(p);
        }
    }
    @Subscribe
    public void onJoin(PostLoginEvent e){
        VelocityMain.getMain().getServer().getScheduler().buildTask(VelocityMain.getMain(), () -> {
            for (Player p : VelocityMain.getMain().getServer().getAllPlayers()){
                MessageSender.sendFooter(p);
            }
        }).delay(2, TimeUnit.SECONDS).schedule();
    }
}
