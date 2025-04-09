package de.lioncraft.velocityMain.messageHandling;

import com.velocitypowered.api.proxy.Player;
import de.lioncraft.velocityMain.utils.GUIElementRenderer;
import net.kyori.adventure.audience.Audience;

import java.util.TimeZone;

public final class MessageSender {
    private MessageSender(){}
    public static void sendHeader(Audience a){
        a.sendPlayerListHeader(GUIElementRenderer.getHeader(TimeZone.getDefault().getID()));
    }
    public static void sendFooter(Player p){
        p.sendPlayerListFooter(GUIElementRenderer.getFooter(p.getCurrentServer().get().getServer().getServerInfo().getName()));
    }
}
