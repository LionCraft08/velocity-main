package de.lioncraft.velocityMain.utils;

import com.velocitypowered.api.proxy.Player;
import de.lioncraft.lionapi.messages.ColorGradient;
import de.lioncraft.velocityMain.VelocityMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class GUIElementRenderer {
    private GUIElementRenderer(){}
    private static Component line, ls;
    public static Component getHeader(String timeZone){
        if (timeZone.isBlank()) timeZone = TimeZone.getDefault().getID();
        return Component.text("").appendNewline().append(ls).appendNewline().appendNewline().append(getTime(timeZone)).appendNewline().append(line);
    }

    public static Component getFooter(String servername){
        return line.appendNewline().append(Component.text("Server: "+servername, TextColor.color(0, 255, 255))).appendNewline().appendNewline()
                .append(Component.text("Netzwerk", TextColor.color(0, 200, 255), TextDecoration.UNDERLINED)).appendNewline().appendNewline()
                .append(Component.text("Player: "+ VelocityMain.getMain().getServer().getAllPlayers().size(), TextColor.color(0, 150, 255)))
                .append(Component.text("        Server: "+VelocityMain.getMain().getServer().getAllServers().size(), TextColor.color(0, 100, 255))).appendNewline();
    }

    public static void init(){
        line = ColorGradient.getNewGradiant("-----------------------------", TextColor.color(250, 0, 255), TextColor.color(0, 0, 255));
        ls = ColorGradient.getNewGradiant("LionSystems Servernetzwerk", TextColor.color(0, 100, 255), TextColor.color(0, 255, 255));

    }
    public static Component getTime(String timeZone){
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZone));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy    HH:mm z");
        return ColorGradient.getNewGradiant(zonedDateTime.format(formatter), TextColor.color(128, 0, 255), TextColor.color(255, 0, 128));
    }
}
