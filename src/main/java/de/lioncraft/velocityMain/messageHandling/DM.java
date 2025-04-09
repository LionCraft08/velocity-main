package de.lioncraft.velocityMain.messageHandling;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public final class DM {
    private DM(){}
    private static Component prefix = getPrefix();
    public static Component info(String text){
        return prefix.append(Component.text(text));
    }

    private static Component getPrefix(){
        Component c = Component.text("<", TextColor.color(255, 255, 255));
        return c.append(Component.text("L", TextColor.color(255, 150, 0)))
                .append(Component.text("i", TextColor.color(255, 130, 0)))
                .append(Component.text("o", TextColor.color(255, 110, 0)))
                .append(Component.text("n", TextColor.color(255, 90, 0)))
                .append(Component.text("V", TextColor.color(255, 70, 0)))
                .append(Component.text("C", TextColor.color(255, 50, 0)))
                .append(Component.text("> ", TextColor.color(255, 255, 255)));
    }
}
