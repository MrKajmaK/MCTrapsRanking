package eu.mctraps.ranking.listeners;

import eu.mctraps.MCTrapsDisplayer.DisplayerAPI;
import eu.mctraps.ranking.Ranking;
import eu.mctraps.ranking.RankingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static eu.mctraps.ranking.Ranking.colorify;
import static eu.mctraps.ranking.Ranking.round;

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
            double killer_set = 0;
            double victim_set = 0;
            Random rand = new Random();

            if(killer_rank <= victim_rank) {
                killer_set = victim_rank * (ThreadLocalRandom.current().nextDouble(killerMin, killerMax + 1) / 100);
            } else if(killer_rank > victim_rank) {
                killer_set = ThreadLocalRandom.current().nextDouble(negMin, negMax + 1);

                if(rand.nextInt(100) < negChance) {
                    killer_set = -killer_set;
                }
            }

            if(killer_set > 0) {
                victim_set = (killer_set * (ThreadLocalRandom.current().nextDouble(loseMin, loseMax + 1) / 100));
            }

            if(killer_set > 30) {
                killer_set = 30;
            }

            killer_set = round(killer_set, 1);
            victim_set = round(victim_set, 1);

            RankingManager.setRank(name, victim_rank - victim_set, plugin);
            RankingManager.setRank(killer, killer_rank + killer_set, plugin);

            plugin.rankingTop = RankingManager.top(plugin);

            e.setDeathMessage("");
            String title = plugin.config.getString("messages.onKill.title").replaceAll("%name%", name);
            String titleplus = plugin.config.getString("messages.onKill.subtitle.plus").replaceAll("%points%", String.valueOf(killer_set));
            String titleminus = plugin.config.getString("messages.onKill.subtitle.minus").replaceAll("%points%", String.valueOf(-killer_set));
            String chatplus = colorify(plugin.config.getString("messages.onKill.chat.plus").replaceAll("%killed%", name).replaceAll("%killed_points%", String.valueOf(victim_set)).replaceAll("%killer%", killer).replaceAll("%killer_points%", String.valueOf(killer_set)));
            String chatminus = colorify(plugin.config.getString("messages.onKill.chat.minus").replaceAll("%killed%", name).replaceAll("%killed_points%", String.valueOf(victim_set)).replaceAll("%killer%", killer).replaceAll("%killer_points%", String.valueOf(-killer_set)));
            if(killer_set > 0) {
                plugin.getServer().broadcastMessage(chatplus);
                plugin.displayer.Title(e.getEntity().getKiller(), title, titleplus);
            } else {
                plugin.getServer().broadcastMessage(chatminus);
                plugin.displayer.Title(e.getEntity().getKiller(), title, titleminus);
            }

        }
    }
}