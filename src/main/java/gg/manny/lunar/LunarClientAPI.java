package gg.manny.lunar;

import gg.manny.lunar.command.TestCommand;
import gg.manny.lunar.listener.PlayerListener;
import gg.manny.lunar.type.ClientType;
import gg.manny.quantum.Quantum;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LunarClientAPI extends JavaPlugin {

    @Getter
    public static LunarClientAPI instance;

    @Getter
    private Map<UUID, ClientType> clientMap = new HashMap<>(); //todo add more clients

    @Override
    public void onEnable() {
        instance = this;

        Quantum quantum = new Quantum(this);
        quantum.registerCommand(new TestCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public ClientType getClient(Player player) {
        return this.clientMap.getOrDefault(player.getUniqueId(), ClientType.NONE);
    }


}
