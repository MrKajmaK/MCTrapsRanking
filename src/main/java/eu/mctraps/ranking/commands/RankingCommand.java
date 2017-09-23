package eu.mctraps.ranking.commands;

import eu.mctraps.ranking.Ranking;
import eu.mctraps.ranking.RankingManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankingCommand implements CommandExecutor {
    Ranking plugin;

    public RankingCommand(Ranking plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("ranking").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            if(sender instanceof Player) {
                Player p = (Player)sender;
                String name = p.getName();
                int kills = RankingManager.getKills(name, plugin);
                int deaths = RankingManager.getDeaths(name, plugin);
                int rank = RankingManager.getRank(name, plugin);
                float kd = kills / deaths;

                sender.sendMessage("§8§m--------§8§l« §9§lTWOJE STATYSTYKI §8§l»§8§m--------");
                sender.sendMessage("§8● §7Ranking §8§l» §6" + rank);
                sender.sendMessage("§8● §7Zabojstwa §8§l» §6" + kills);
                sender.sendMessage("§8● §7Smierci §8§l» §6" + deaths);
                sender.sendMessage("§8● §7K/D §8§l» §6" + kd);
                sender.sendMessage("§8§m-----------------------------------------");
            } else {
                sender.sendMessage("§cPoprawne uzycie: §7/" + label + " §c<nick>");
            }
        } else {
            if(args[0].equalsIgnoreCase("top")) {
                // ToDo: top
            } else {
                if(RankingManager.exists(args[0], plugin)) {
                    String name = args[0];
                    int kills = RankingManager.getKills(name, plugin);
                    int deaths = RankingManager.getDeaths(name, plugin);
                    int rank = RankingManager.getRank(name, plugin);
                    float kd = kills / deaths;

                    sender.sendMessage("§8§m--------§8§l« §9§lSTATYSTYKI§9: §7" + name + " §8§l»§8§m--------");
                    sender.sendMessage("§8● §7Ranking §8§l» §6" + rank);
                    sender.sendMessage("§8● §7Zabojstwa §8§l» §6" + kills);
                    sender.sendMessage("§8● §7Smierci §8§l» §6" + deaths);
                    sender.sendMessage("§8● §7K/D §8§l» §6" + kd);
                    sender.sendMessage("§8§m-----------------------------------------");
                } else {
                    sender.sendMessage("§cBlad: §4taki gracz nie istnieje");
                }
            }
        }
        return false;
    }
}