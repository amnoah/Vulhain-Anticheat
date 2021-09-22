package me.salers.vulhain.listener;

import me.salers.vulhain.Vulhain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws InstantiationException, IllegalAccessException {
        //adding the player's data in the cache

        Vulhain.getInstance().getPlayerDataManager().add(event.getPlayer().getUniqueId());


    }



    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        //removing the player's data from the cache
        Vulhain.getInstance().getPlayerDataManager().remove(event.getPlayer().getUniqueId());
    }
}
