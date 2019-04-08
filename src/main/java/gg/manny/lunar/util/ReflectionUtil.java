package gg.manny.lunar.util;

import gg.manny.lunar.handler.PacketHandler;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * for random stuff
 */
public class ReflectionUtil {

    public static Class<?> getClass(String className) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + className);
    }

    public static void sendPacket(Player player, Object packet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ClassNotFoundException {
        Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
        Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        playerConnection.getClass().getMethod("sendPacket", getClass("Packet")).invoke(playerConnection, packet);
    }

    public void setField(Object instance, String field, Object value) {
        try {
            Field fieldObject = instance.getClass().getDeclaredField(field);
            fieldObject.setAccessible(true);
            fieldObject.set(instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getField(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static Object getMethod(Object instance, String methodName) throws Exception {
        Method method = instance.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(instance);
    }

    public static Channel getChannel(Player player) throws Exception {
        Object nmsPlayer = player.getClass().getMethod("getHandle").invoke(player);
        Object playerConnection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        Object networkManager = getField(playerConnection, "networkManager");

        Channel channel = null;
        try {
            channel = (Channel) getField(networkManager, "m");
            if (channel == null) {
                channel = (Channel) getField(networkManager, "i");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return channel;
    }

    public static void inject(Player player) throws Exception {
        Channel channel = getChannel(player);
        if (channel != null) {
            channel.pipeline().addBefore("packet_handler", "manny", new PacketHandler(player));
        }
    }

    public static void eject(Player player) throws Exception {
        Channel channel = getChannel(player);
        if (channel != null) {
            channel.pipeline().remove("manny");
        }
    }


}
