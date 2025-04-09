package de.lioncraft.velocityMain;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.VelocityBrigadierMessage;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import de.lioncraft.velocityMain.commands.DebugCommand;
import de.lioncraft.velocityMain.commands.LobbyCommand;
import de.lioncraft.velocityMain.listeners.BackendListeners;
import de.lioncraft.velocityMain.listeners.PacketListeners;
import de.lioncraft.velocityMain.listeners.PlayerListeners;
import de.lioncraft.velocityMain.utils.GUIElementRenderer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Plugin(id = "velocitymain", name = "VelocityMain", version = "1.0.0", description = "Basic functions and commands for Velocity", authors = {"LionCraft"})
public class VelocityMain {

    private static VelocityMain main;
    private Logger logger;
    private ProxyServer server;

    @Inject
    public VelocityMain(ProxyServer server, Logger logger){
        main=this;
        this.server = server;
        this.logger = logger;
        logger.info("Enabled LionVelocity Plugin ");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getEventManager().register(this, new PlayerListeners());
        server.getEventManager().register(this, new BackendListeners());
        server.getEventManager().register(this, new PacketListeners());

        server.getChannelRegistrar().register(BackendListeners.IDENTIFIER);
        server.getChannelRegistrar().register(BackendListeners.FAVICONS);

        CommandManager cm = server.getCommandManager();
        cm.register(cm.metaBuilder("lobby").aliases("l").plugin(this).build(), new LobbyCommand());
        cm.register(cm.metaBuilder("debug").plugin(this).build(), new DebugCommand());

        GUIElementRenderer.init();
        server.getScheduler().buildTask(this, () -> {
            for (Player p : server.getAllPlayers()){
                p.sendPlayerListHeader(GUIElementRenderer.getHeader(TimeZone.getDefault().getID()));
            }
        }).repeat(Duration.ofMinutes(1)).delay(60-Calendar.getInstance().get(Calendar.SECOND), TimeUnit.SECONDS).schedule();
    }

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getServer() {
        return server;
    }

    public static VelocityMain getMain() {
        return main;
    }
}
