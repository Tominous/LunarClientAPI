package gg.manny.lunar;

import gg.manny.lunar.command.CheckCommand;
import gg.manny.lunar.listener.PlayerListener;
import gg.manny.lunar.util.ReflectionUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class LunarClientAPI extends JavaPlugin {

    @Getter
    public static LunarClientAPI instance;

    private List<UUID> players = new ArrayList<>();

    private boolean restrict;
    private String kickMessage;
    private String authMessage;

    @Override
    public void onEnable() {
        instance = this;

        this.loadConfig();

        ReflectionUtil.registerCommand(this, new CheckCommand(this));
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }


    private void loadConfig() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        this.restrict = this.getConfig().getBoolean("restrict", false);
        this.kickMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("kick-message", "&cYou must use Lunar Client to connect to this server."));
        this.authMessage = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("authenticate", " \n&aYou have connected to the server with &lLunar Client&a.\n "));
    }

    public boolean onClient(Player player) {
        return this.players.contains(player.getUniqueId());
    }


}
