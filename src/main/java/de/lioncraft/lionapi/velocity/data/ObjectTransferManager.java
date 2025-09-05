package de.lioncraft.lionapi.velocity.data;

import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import de.lioncraft.velocityMain.listeners.BackendListeners;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObjectTransferManager {
    private static final List<EventListener> globalListeners = new ArrayList<>();
    private static final HashMap<String, List<EventListener>> objectSpecificListeners = new HashMap<>();

    public static void sendObject(TransferrableObject to, ServerConnection sc){
        sc.sendPluginMessage(BackendListeners.OBJECT_TRANSFER, to.toString().getBytes());
    }

    public static void onObjectReceive(String object, ChannelMessageSource source){
        TransferrableObject to = TransferrableObject.getFromJson(object);
        runThrough(globalListeners, to, source);
        objectSpecificListeners.forEach((s, eventListeners) -> {
            if (s.equals(to.getObjectType())){
                runThrough(eventListeners, to, source);
            }
        });
    }

    private static void runThrough(List<EventListener> list, TransferrableObject to, ChannelMessageSource source){
        list.forEach(eventListener -> {
            try {
                eventListener.onReceive(to, source);
            } catch (Exception e) {
                System.err.println("Error during Event Listener for Object " + to.getObjectType() + ": " + e.getMessage());
            }
        });
    }

    public static void registerListener(@Nullable String objectType, EventListener listener){
        if (objectType == null){
            globalListeners.add(listener);
        }else{
            if (!objectSpecificListeners.containsKey(objectType)) objectSpecificListeners.put(objectType, new ArrayList<>());
            objectSpecificListeners.get(objectType).add(listener);
        }
    }
}
