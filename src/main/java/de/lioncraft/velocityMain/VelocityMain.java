package de.lioncraft.velocityMain;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.lioncraft.velocityMain.commands.DebugCommand;
import de.lioncraft.velocityMain.commands.LobbyCommand;
import de.lioncraft.velocityMain.data.Config;
import de.lioncraft.velocityMain.listeners.BackendListeners;
import de.lioncraft.velocityMain.listeners.PacketListeners;
import de.lioncraft.velocityMain.listeners.PlayerListeners;
import de.lioncraft.velocityMain.utils.GUIElementRenderer;
import de.lioncraft.velocityMain.utils.ServerStorageHandler;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Plugin(id = "velocitymain", name = "VelocityMain", version = "1.0.0", description = "Basic functions and commands for Velocity", authors = {"LionCraft"})
public class VelocityMain {

    private static VelocityMain main;
    private Logger logger;
    private ProxyServer server;
    private Path dataDirectory;

    @Inject
    public VelocityMain(ProxyServer server, Logger logger, @DataDirectory java.nio.file.Path dataDirectory){
        main=this;
        this.server = server;
        this.logger = logger;
        logger.info("Enabled LionVelocity Plugin ");
        this.dataDirectory = dataDirectory;
        dataDirectory.toFile().mkdirs();
        saveResourceIfNotExists("config.json", Paths.get(dataDirectory.toString(), "config.json"));
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
        try {
            Config.loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ServerStorageHandler.launch();
    }

    public Logger getLogger() {
        return logger;
    }

    public ProxyServer getServer() {
        return server;
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }

    public static VelocityMain getMain() {
        return main;
    }
    public static boolean saveResourceIfNotExists(String resource, Path outputPath) {
        if (resource == null || resource.isEmpty() || !resource.startsWith("/")) {
            System.err.println("Error: Resource path must be a non-empty absolute path starting with '/' (e.g., /com/example/file.txt).");
            return false;
        }

        File targetFile = outputPath.toFile();

        if (targetFile.exists()) {
            if (targetFile.isDirectory()) {
                return false;
            }
            return false;
        }
        try (InputStream resourceStream = VelocityMain.class.getResourceAsStream(resource)) {

            if (resourceStream == null) {
                return false;
            }

            Path parentDir = outputPath.getParent();
            if (parentDir != null) {
                if (!Files.exists(parentDir)) {
                    Files.createDirectories(parentDir);
                }
            }

            Files.copy(resourceStream, outputPath);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving resource '" + resource + "' to '" + outputPath + "': " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (SecurityException e) {
            System.err.println("Error creating directories for '" + outputPath + "' due to security restrictions: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
