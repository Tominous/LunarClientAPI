package gg.manny.lunar;

import gg.manny.lunar.command.TestCommand;
import gg.manny.lunar.type.ClientType;
import gg.manny.lunar.util.ReflectionUtil;
import gg.manny.quantum.Quantum;
import lombok.Getter;
import net.minecraft.util.io.netty.buffer.ByteBuf;
import net.minecraft.util.io.netty.buffer.Unpooled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LunarClientAPI extends JavaPlugin implements Listener {

    @Getter
    public static LunarClientAPI instance;

    @Getter
    private Map<UUID, ClientType> clientMap = new HashMap<>(); //todo add more clients

    @Override
    public void onEnable() {
        instance = this;

        Quantum quantum = new Quantum(this);
        quantum.registerCommand(new TestCommand(this));

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public ClientType getClient(Player player) {
        return this.clientMap.getOrDefault(player.getUniqueId(), ClientType.NONE);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //String ByteBuf
        //Packet packet = new PacketPlayOutCustomPayload("REGISTER", PivotUtil.serializeBuf("Lunar-Client"));
        this.clientMap.put(player.getUniqueId(), ClientType.NONE);
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            //messy af ¯\_(ツ)_/¯
            try {
                Constructor constructor = ReflectionUtil.getClass("PacketPlayOutCustomPayload").getConstructor(String.class, ByteBuf.class);
                Constructor serializerConstructor = ReflectionUtil.getClass("PacketDataSerializer").getConstructor(ByteBuf.class);
                Object packet = constructor.newInstance("REGISTER", serializerConstructor.newInstance(Unpooled.wrappedBuffer("Lunar-Client".getBytes())));
                ReflectionUtil.sendPacket(player, packet);
                ReflectionUtil.inject(player);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        try {
            ReflectionUtil.eject(event.getPlayer());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.clientMap.remove(event.getPlayer().getUniqueId());
    }

}
