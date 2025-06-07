package de.lioncraft.velocityMain.utils;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.lioncraft.velocityMain.VelocityMain;
import de.lioncraft.velocityMain.data.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerStorageHandler {
    private static List<StoredServer> servers = new ArrayList<>();
    public static void launch(){
        VelocityMain.getMain().getServer().getScheduler().buildTask(VelocityMain.getMain(), () -> {
            VelocityMain.getMain().getServer().getAllServers().forEach(ServerStorageHandler::sendSinglePing);
                })
                .repeat(Config.getInstance().getServerPingDelay(), TimeUnit.SECONDS)
                .delay(1, TimeUnit.SECONDS)
                .schedule();
    }

    public static List<StoredServer> getServers() {
        return servers;
    }
    public static StoredServer getServer(String name){
        for (StoredServer server : servers){
            if (server.getServer().getServerInfo().getName().equals(name)) return server;
        }
        return null;
    }
    public static StoredServer getServer(RegisteredServer rs){
        for (StoredServer server : servers){
            if (server.getServer().equals(rs)) return server;
        }
        return null;
    }
    public static void addServer(StoredServer server){
        if(!servers.contains(server)) servers.add(server);
    }

    public static void sendSinglePing(RegisteredServer rs){
        rs.ping().orTimeout(3, TimeUnit.SECONDS).handle((serverPing, throwable) -> {
            if (serverPing != null){
                StoredServer storedServer = getServer(rs);
                StoredServer newServer = new StoredServer(rs,
                        serverPing.getFavicon().orElse(null),
                        serverPing.getVersion(),
                        serverPing.getDescriptionComponent(),
                        serverPing.getPlayers().orElse(null));

                if (storedServer != null){
                    if (!storedServer.equals(newServer)){
                        servers.remove(storedServer);
                        servers.add(newServer);
                        sendNewServerState(newServer, true);
                    }
                }

            }
            return null;
        });
    }

    private static void sendPings(){
        for (RegisteredServer rs : VelocityMain.getMain().getServer().getAllServers()){
            sendSinglePing(rs);
        }
    }
    public static void sendNewServerState(StoredServer server, boolean isOnline){

    }
}
