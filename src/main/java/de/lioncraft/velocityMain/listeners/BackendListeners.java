package de.lioncraft.velocityMain.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.server.ServerRegisteredEvent;
import com.velocitypowered.api.event.proxy.server.ServerUnregisteredEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.PingOptions;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import de.lioncraft.lionapi.messages.DM;
import de.lioncraft.velocityMain.VelocityMain;
import de.lioncraft.velocityMain.utils.ServerStorageHandler;
import de.lioncraft.velocityMain.utils.StoredServer;

import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BackendListeners {

    public static final MinecraftChannelIdentifier IDENTIFIER = MinecraftChannelIdentifier.create("lionapi", "main");
    public static final MinecraftChannelIdentifier FAVICONS = MinecraftChannelIdentifier.create("lionapi","favicons");

    @Subscribe
    public void onPluginMessageFromBackend(PluginMessageEvent event) {
        if (!IDENTIFIER.equals(event.getIdentifier())) return;

        VelocityMain.getMain().getLogger().debug("Handling Input: {}", new String(event.getData()));

        event.setResult(PluginMessageEvent.ForwardResult.handled());

        ServerConnection backend = null;
        if (event.getSource() instanceof ServerConnection sc)  backend = sc;
        if (event.getSource() instanceof Player p){
            backend = p.getCurrentServer().get();
        }
        assert backend != null;
        String command = new String(event.getData());
        if (command.equalsIgnoreCase("lobby.getservers")){
            ServerConnection finalBackend = backend;
            VelocityMain.getMain().getServer().getScheduler().buildTask(VelocityMain.getMain(), () -> {
                for (RegisteredServer server : VelocityMain.getMain().getServer().getAllServers()){
                    StringBuilder s = new StringBuilder();
                    s.append(server.getServerInfo().getName()).append(":");
                    Favicon f = null;
                    try {
                        ServerPing serverPing = server.ping().get(3L, TimeUnit.SECONDS);
                        if (serverPing != null){
                            s.append(true);
                            if(serverPing.getFavicon().isPresent()) f = serverPing.getFavicon().get();
                        }
                    } catch (InterruptedException | TimeoutException |ExecutionException e) {
                        s.append(false);
                    }
                    boolean b = sendPluginMessageToBackend(finalBackend.getServer(), IDENTIFIER, s.toString().getBytes());
                    boolean b2 = false;
                    if (f != null) {
                        String fav = server.getServerInfo().getName() + ":" + f.getBase64Url();
                        b2 = sendPluginMessageToBackend(finalBackend.getServer(), FAVICONS, fav.getBytes());
                    }
                    VelocityMain.getMain().getLogger().info("Sent message {} to Backend: Result: {}; {}", s, b, b2);
                }
            }).schedule();
        } else if (command.startsWith("lobby.connect")) {
            String[] data = command.split(" ");
            String player = data[1];
            RegisteredServer server = VelocityMain.getMain().getServer().getServer(data[2]).orElse(null);
            Player p = VelocityMain.getMain().getServer().getPlayer(player).orElse(null);
            if (p != null){
                if (server != null){
                    if (!p.getCurrentServer().get().getServer().equals(server)){
                        p.createConnectionRequest(server).fireAndForget();
                    }else p.sendMessage(DM.error("Du bist bereits mit diesem Server verbunden"));
                }else p.sendMessage(DM.error("Dieser Server kann zur Zeit nicht erreicht werden."));
            }
        }
    }

    public boolean sendPluginMessageToBackend(RegisteredServer server, ChannelIdentifier identifier, byte[] data) {
        return server.sendPluginMessage(identifier, data);
    }

    @Subscribe
    public void onRegister(ServerRegisteredEvent e){
        ServerStorageHandler.sendSinglePing(e.registeredServer());
    }
    @Subscribe
    public void onUnregister(ServerUnregisteredEvent e){
        ServerStorageHandler.sendSinglePing(e.unregisteredServer());
    }
}
