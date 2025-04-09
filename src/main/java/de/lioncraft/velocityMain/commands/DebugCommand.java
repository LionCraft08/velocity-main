package de.lioncraft.velocityMain.commands;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.lioncraft.velocityMain.VelocityMain;

import java.util.concurrent.ExecutionException;

public class DebugCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        VelocityMain.getMain().getServer().getScheduler().buildTask(VelocityMain.getMain(), () -> {
            try {
                ServerPing ping = VelocityMain.getMain().getServer().getServer("lobby").get().ping().get();
                VelocityMain.getMain().getLogger().info(ping.getFavicon().get().toString());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }).schedule();

    }
}
