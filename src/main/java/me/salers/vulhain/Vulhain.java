package me.salers.vulhain;


import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.utils.server.ServerVersion;
import lombok.Getter;
import me.salers.vulhain.data.PlayerDataManager;
import me.salers.vulhain.listener.BukkitListener;
import me.salers.vulhain.listener.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Vulhain extends JavaPlugin {

    @Getter
    private static Vulhain instance;

    @Override
    public void onLoad() {
        PacketEvents.create(this).getSettings()
                .bStats(true)
                .checkForUpdates(false)
                .compatInjector(false)
                .fallbackServerVersion(ServerVersion.v_1_8_8);
    }

    @Override
    public void onEnable() {
        instance = this;
        loadEvents();
        saveDefaultConfig();
        PacketEvents.get().init();
        PacketEvents.get().registerListener(new PacketListener());
    }

    @Override
    public void onDisable() {
        instance = null; //for preventing memory leaks
    }

    /**
     * Getting the instance of PlayerDataManager
     *
     * @return the instance of PlayerDataManager
     **/

    public PlayerDataManager getPlayerDataManager() {
        return PlayerDataManager.getInstance();
    }


    /**
     * Loading all listeners that we will need
     **/

    private void loadEvents() {
        Bukkit.getPluginManager().registerEvents(new BukkitListener(), this);
        new PacketListener();
    }
}
