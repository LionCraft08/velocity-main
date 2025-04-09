package de.lioncraft.velocityMain.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.velocitypowered.api.command.BrigadierCommand;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.lioncraft.velocityMain.VelocityMain;
import de.lioncraft.velocityMain.messageHandling.DM;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class LobbyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player p){
            String lobby = "lobby";
            if(invocation.arguments().length>= 1){
                try {
                    int i = Integer.parseInt(invocation.arguments()[0]);
                    lobby = lobby+i;
                }catch (NumberFormatException e){
                }
            }
            Optional<RegisteredServer> server = VelocityMain.getMain().getServer().getServer(lobby);
            if (server.isPresent()){
                if (!p.getCurrentServer().get().getServer().equals(server.get())){
                    p.createConnectionRequest(server.get()).connectWithIndication();
                }else p.sendMessage(DM.info("You are already connected to this Server"));
            }else p.sendMessage(DM.info("This Server does not exist"));
        }else invocation.source().sendMessage(DM.info("You are not a Player"));

    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return SimpleCommand.super.suggest(invocation);
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        List<String> list = new ArrayList<>();
        if (invocation.arguments().length > 1) return CompletableFuture.completedFuture(list);
        list.add("main");
        for(RegisteredServer rs : VelocityMain.getMain().getServer().getAllServers()){
            String s = rs.getServerInfo().getName();
            if (s.startsWith("lobby")){
                s = s.replaceFirst("lobby", "");
                if (!s.isBlank()) list.add(s);
            }
        }
        return CompletableFuture.completedFuture(list);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return SimpleCommand.super.hasPermission(invocation);
    }
}
