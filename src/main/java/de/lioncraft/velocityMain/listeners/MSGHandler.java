package de.lioncraft.velocityMain.listeners;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import de.lioncraft.lionapi.messageHandling.MSG;
import de.lioncraft.lionapi.messageHandling.lionchat.LionChat;
import de.lioncraft.lionapi.velocity.data.EventListener;
import de.lioncraft.lionapi.velocity.data.TransferrableObject;
import de.lioncraft.velocityMain.VelocityMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;

import java.util.Optional;
import java.util.UUID;

/**
 * For TransferableObject Type "msg"
 * Data at: source, message, target, sourcePlayer
 */
public class MSGHandler implements EventListener {
    @Override
    public void onReceive(TransferrableObject object, ChannelMessageSource src) {
        Component source = JSONComponentSerializer.json().deserialize(object.getData().get("source"));
        Component message = JSONComponentSerializer.json().deserialize(object.getData().get("message"));
        UUID target = UUID.fromString(object.getData().get("target"));
        UUID srcPlayerID = UUID.fromString(object.getData().get("sourcePlayer"));
        Optional<Player> p = VelocityMain.getMain().getServer().getPlayer(target);
        Optional<Player> srcPlayer = VelocityMain.getMain().getServer().getPlayer(srcPlayerID);
        if (p.isEmpty()){
            LionChat.sendMSG(null, MSG.noPlayer.getText(), srcPlayer.get());
        }else{
            LionChat.sendMSG(source, message, p.get());
            LionChat.sendMSG(Component.text("Du -> ").append(Component.text(p.get().getUsername())), message, srcPlayer.get());

        }
    }
}
