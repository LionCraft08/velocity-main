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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class StoredServer {


    private RegisteredServer server;
    private Favicon icon;
    private ServerPing.Version version;
    private Component MOTD;
    private ServerPing.Players playerInfo;

    public StoredServer(RegisteredServer server, @Nullable Favicon icon, ServerPing.Version version, Component MOTD, ServerPing.Players playerInfo) {
        this.server = server;
        this.icon = icon;
        this.version = version;
        this.MOTD = MOTD;
        this.playerInfo = playerInfo;
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

    public ServerPing.Players getPlayerInfo() {
        return playerInfo;
    }
    public String toString(){
        return server.getServerInfo().getName()+":"+icon.getBase64Url()+":"+version.getName()+":"+MOTD.toString()+":"+playerInfo.toString();
    }

    public boolean equals(StoredServer other){
        if (other == null) return false;
        if (other.server == null) return false;
        if (other.server.getServerInfo().getName().equals(server.getServerInfo().getName())
        &&other.icon.equals(getIcon())
        &&other.version.equals(version)
        &&other.playerInfo.equals(playerInfo)
        &&other.getMOTD().equals(getMOTD())) return true;
        return false;
    }
}
