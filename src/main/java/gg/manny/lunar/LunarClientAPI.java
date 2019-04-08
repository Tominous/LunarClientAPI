package gg.manny.lunar;

import gg.manny.lunar.command.CheckCommand;
import gg.manny.lunar.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LunarClientAPI extends JavaPlugin {

    @Getter
    public static LunarClientAPI instance;

    @Getter
    private static List<UUID> players = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;

        this.registerCommand(new CheckCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public static boolean onClient(Player player) {
        return players.contains(player.getUniqueId());
    }

    private void registerCommand(Command command) {
        Field bukkitCommandMap = null;
        try {
            bukkitCommandMap = this.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

        CommandMap commandMap;
        try {
            commandMap = (org.bukkit.command.CommandMap) bukkitCommandMap.get(this.getServer());
            commandMap.register(this.getDescription().getName(), command);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
