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
        int killerMin = plugin.config.getInt("percentages.killerMin");
        int killerMax = plugin.config.getInt("percentages.killerMax");

        int negMin = plugin.config.getInt("percentages.negMin");
        int negMax = plugin.config.getInt("percentages.negMax");
        int negChance = plugin.config.getInt("percentages.negChance");

        int loseMin = plugin.config.getInt("percentages.loseMin");
        int loseMax = plugin.config.getInt("percentages.loseMax");

        int negLoseMin = plugin.config.getInt("percentages.negLoseMin");
        int negLoseMax = plugin.config.getInt("percentages.negLoseMax");

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
            int victim_set = 0;
            Random rand = new Random();

            if(killer_rank <= victim_rank) {
                killer_set = ((victim_rank - killer_rank) * (rand.nextInt(((killerMax - killerMin) + 1) + killerMin) / 100));
//                killer_set = killer_rank;
                plugin.getServer().getPlayer("KajmaczeK").sendMessage("killerMax: " + killerMax);
                plugin.getServer().getPlayer("KajmaczeK").sendMessage("killerMin: " + killerMin);
                plugin.getServer().getPlayer("KajmaczeK").sendMessage("calc: " + (rand.nextInt(((killerMax - killerMin) + 1) + killerMin) / 100));
            } else if(killer_rank > victim_rank) {
                killer_set = rand.nextInt(((negMax - negMin) + 1) + negMin);

                if(rand.nextInt(100) < negChance) {
                    killer_set = -killer_set;
                }
            }

            if(killer_set > 0) {
                victim_set = (killer_set * rand.nextInt(((loseMax - loseMin) + 1) + loseMin));
            }

            plugin.getServer().getPlayer("KajmaczeK").sendMessage("victim_set: " + victim_set);
            plugin.getServer().getPlayer("KajmaczeK").sendMessage("killer_set: " + killer_set);
            plugin.getServer().getPlayer("KajmaczeK").sendMessage("victim_rank: " + victim_rank);
            plugin.getServer().getPlayer("KajmaczeK").sendMessage("killer_rank: " + killer_rank);

//            if(killer_rank < victim_rank) {
//                victim_set =
//            }

//            killer_set = (int)(victim_rank * plugin.config.getDouble("percentage"));
//            if(killer_set < 0) {
//                killer_set = -killer_set;
//            }

//            if(killer_rank > 1000 && killer_set > 32) {
//                killer_set = 32;
//            }

            RankingManager.setRank(name, victim_rank - victim_set, plugin);
            RankingManager.setRank(killer, killer_rank + killer_set, plugin);
            
            e.setDeathMessage("");
            plugin.getServer().broadcastMessage("§cGracz: §7" + name + " §8(§c§l-§7" + victim_set + "§8) §czostal zabity przez: §7" + killer + " §8(§a+§7" + killer_set + "§8)");
        }
    }
}