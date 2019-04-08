package gg.manny.lunar.listener;

import gg.manny.lunar.LunarClientAPI;
import gg.manny.lunar.handler.PacketHandler;
import gg.manny.lunar.util.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static gg.manny.lunar.util.ReflectionUtil.getChannel;

@RequiredArgsConstructor
public class PlayerListener implements Listener {

    private final LunarClientAPI instance;

    private final Executor INJECT_EXECUTOR = Executors.newSingleThreadExecutor();
    private final Executor EJECT_EXECUTOR = Executors.newSingleThreadExecutor(); //when u gotta pull out faster than ygore

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        INJECT_EXECUTOR.execute(() -> {
            // ¯\_(ツ)_/¯
            try {
                Constructor constructor = ReflectionUtil.getClass("PacketPlayOutCustomPayload").getConstructor(String.class, ByteBuf.class);
                Constructor serializerConstructor = ReflectionUtil.getClass("PacketDataSerializer").getConstructor(ByteBuf.class);
                Object packet = constructor.newInstance("REGISTER", serializerConstructor.newInstance(Unpooled.wrappedBuffer("Lunar-Client".getBytes())));
                ReflectionUtil.sendPacket(player, packet);
                Channel channel = getChannel(player);
                if (channel != null) {
                    channel.pipeline().addBefore("packet_handler", "manny", new PacketHandler(instance, player));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        instance.getPlayers().remove(player.getUniqueId());

        EJECT_EXECUTOR.execute(() -> {
            try {
                Channel channel = getChannel(player);
                if (channel != null && channel.pipeline().get("manny") != null) {
                    channel.pipeline().remove("manny");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}