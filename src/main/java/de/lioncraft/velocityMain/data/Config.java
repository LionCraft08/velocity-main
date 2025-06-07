package de.lioncraft.velocityMain.data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.lioncraft.velocityMain.VelocityMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

public class Config {
    private static Config instance;
    private int serverPingDelay;
    private String networkName;
    private List<String> motds;
    public Config(int serverPingDelay, String networkName, List<String> motds) {
        this.serverPingDelay = serverPingDelay;
        this.networkName = networkName;
        this.motds = motds;
    }
    public static void loadConfig() throws IOException {
        instance = new Gson().fromJson(new String(Files.readAllBytes(VelocityMain.getMain().getDataDirectory().resolve("config.yml"))), Config.class);
        if (instance == null){
            instance = new Config(10, "<gradient:#0066FF:#00FFFF>LionSystems Servernetzwerk",
                    List.of("<gradient:#FF00FF:#FFFF00>Herzlich Willkommen!", "<gradient:#FF00FF:#FFFF00>a LionCraft Server", "<gradient:#FF00FF:#FFFF00>LionCraft Network"));
            Files.write(VelocityMain.getMain().getDataDirectory().resolve("config.yml"), new Gson().toJson(instance).getBytes());
        }
    }

    public static Config getInstance() {
        return instance;
    }

    public int getServerPingDelay() {
        return serverPingDelay;
    }

    public void setServerPingDelay(int serverPingDelay) {
        this.serverPingDelay = serverPingDelay;
    }

    public String getNetworkName() {
        return networkName;
    }
    public Component getNetworkNameComponent(){
        return MiniMessage.miniMessage().deserialize(networkName);
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public List<String> getMotds() {
        return motds;
    }

    public void setMotds(List<String> motds) {
        this.motds = motds;
    }

    public Component getRandomMotdComponent(){
        return MiniMessage.miniMessage().deserialize(motds.get((int) (Math.random() * motds.size())));
    }

    public Component getMotdComponent(int i){
        return MiniMessage.miniMessage().deserialize(motds.get(i));
    }
}
