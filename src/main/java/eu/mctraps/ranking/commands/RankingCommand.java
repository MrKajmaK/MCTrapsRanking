package eu.mctraps.ranking.commands;

import eu.mctraps.ranking.Ranking;
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

            }
        }
        return false;
    }
}