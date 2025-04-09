package de.lioncraft.velocityMain.utils;

import com.velocitypowered.api.proxy.server.PingOptions;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import de.lioncraft.velocityMain.VelocityMain;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoredServer {
    private static List<StoredServer> servers;
    public static void firstInit(){
        if (servers != null) return;
        servers = new ArrayList<>();
        VelocityMain.getMain().getServer().getScheduler().buildTask(VelocityMain.getMain(), StoredServer::sendPings)
                .delay(1, TimeUnit.SECONDS)
                .repeat(15, TimeUnit.SECONDS)
                .schedule();
    }
    private static void sendPings(){
        for (RegisteredServer rs : VelocityMain.getMain().getServer().getAllServers()){
            sendSinglePing(rs);
        }
    }
    public static void sendSinglePing(RegisteredServer rs){
        rs.ping().orTimeout(3, TimeUnit.SECONDS).handle((serverPing, throwable) -> {
            remove(rs);
            if (serverPing != null){
                servers.add(new StoredServer(rs, serverPing.getFavicon().orElse(null), serverPing.getVersion(), serverPing.getDescriptionComponent()));
            }
            return null;
        });
    }

    public static boolean remove(RegisteredServer rs){
        for (StoredServer s : servers){
            if (s.getServer() == rs){
                servers.remove(s);
                return true;
            }
        }
        return false;
    }


    private RegisteredServer server;
    private Favicon icon;
    private ServerPing.Version version;
    private Component MOTD;

    public StoredServer(RegisteredServer server, @Nullable Favicon icon, ServerPing.Version version, Component MOTD) {
        this.server = server;
        this.icon = icon;
        this.version = version;
        this.MOTD = MOTD;
    }

    public RegisteredServer getServer() {
        return server;
    }

    public Favicon getIcon() {
        return icon;
    }

    public ServerPing.Version getVersion() {
        return version;
    }

    public Component getMOTD() {
        return MOTD;
    }
}
