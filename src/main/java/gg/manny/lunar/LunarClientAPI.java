package gg.manny.lunar;

import gg.manny.lunar.command.TestCommand;
import gg.manny.lunar.listener.PlayerListener;
import gg.manny.quantum.Quantum;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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

        Quantum quantum = new Quantum(this);
        quantum.registerCommand(new TestCommand());

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public static boolean onClient(Player player) {
        return players.contains(player.getUniqueId());
    }

}
