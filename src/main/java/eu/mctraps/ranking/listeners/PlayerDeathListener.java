package eu.mctraps.ranking.listeners;

import eu.mctraps.ranking.Ranking;
import eu.mctraps.ranking.RankingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class PlayerDeathListener implements Listener {
    Ranking plugin;

    public PlayerDeathListener(Ranking plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        String name = e.getEntity().getName();

        int deaths = RankingManager.getDeaths(name, plugin);
        deaths++;
        RankingManager.setDeaths(name, deaths, plugin);

        if(e.getEntity().getKiller() != null) {
            String killer = e.getEntity().getKiller().getName();

            int kills = RankingManager.getKills(killer, plugin);
            kills++;
            RankingManager.setKills(killer, kills, plugin);

            int victim_rank = RankingManager.getRank(name, plugin);
            int killer_rank = RankingManager.getRank(killer, plugin);
            int killer_set = 0;

            killer_set = (int)((killer_rank - victim_rank) * 0.1D);
            if(killer_set < 0) {
                killer_set = -killer_set;
            }

            RankingManager.setRank(name, victim_rank - killer_set, plugin);
            RankingManager.setRank(killer, killer_rank + killer_rank, plugin);
            
            e.setDeathMessage("§cGracz: §7" + name + " §8(§c§l-§7" + killer_set + "§8) §czostal zabity przez: §7" + killer + " §8(§a§l+§7" + killer_set + "§8)");
        }
    }
}