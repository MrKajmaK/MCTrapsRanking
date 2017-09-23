package eu.mctraps.ranking.listeners;

import eu.mctraps.ranking.Ranking;
import eu.mctraps.ranking.RankingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    Ranking plugin;

    public PlayerJoinListener(Ranking plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!RankingManager.exists(e.getPlayer().getName(), plugin)) {
            RankingManager.createProfile(e.getPlayer().getName(), plugin);
        }
    }
}