package gg.manny.lunar.handler;

import gg.manny.lunar.LunarClientAPI;
import gg.manny.lunar.event.PlayerAuthenticateEvent;
import gg.manny.lunar.util.ReflectionUtil;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.io.netty.channel.ChannelDuplexHandler;
import net.minecraft.util.io.netty.channel.ChannelHandlerContext;
import org.apache.commons.codec.binary.StringUtils;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class PacketHandler extends ChannelDuplexHandler {

    private final LunarClientAPI plugin;
    private final Player player;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg.getClass().getSimpleName().equals("PacketPlayInCustomPayload")) {
            byte[] data = (byte[]) ReflectionUtil.getMethod(msg, "e");
            String payload = StringUtils.newStringUtf8(data);
            if (payload.contains("Lunar-Client")) {
                if (!this.plugin.getPlayers().contains(player.getUniqueId())) {
                    this.plugin.getPlayers().add(player.getUniqueId());

                    player.sendMessage(plugin.getAuthMessage());
                    plugin.getServer().getPluginManager().callEvent(new PlayerAuthenticateEvent(player));

                }
            }

        } else {
            super.channelRead(ctx, msg);
        }
    }
}
