package gg.manny.lunar.command;

import gg.manny.lunar.LunarClientAPI;
import gg.manny.quantum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand {

    @Command(names = "check")
    public void execute(CommandSender sender, Player target) {
        sender.sendMessage(target.getName() + " is " + (LunarClientAPI.onClient(target) ? ChatColor.GREEN : "not " + ChatColor.RED) + " Lunar Client" + ChatColor.WHITE + ".");
    }
}
