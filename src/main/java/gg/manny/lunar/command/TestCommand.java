package gg.manny.lunar.command;

import gg.manny.lunar.LunarClientAPI;
import gg.manny.lunar.type.ClientType;
import gg.manny.quantum.command.Command;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TestCommand {

    private final LunarClientAPI plugin;

    @Command(names = "check")
    public void execute(CommandSender sender, Player target) {
        ClientType type = plugin.getClientMap().getOrDefault(target.getUniqueId(), ClientType.NONE);
        sender.sendMessage(target.getName() + " is using client " + type.name());

       // ((CraftPlayer)target).getHandle().playerConnection.networkManager
    }
}
