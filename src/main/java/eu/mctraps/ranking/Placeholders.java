package eu.mctraps.ranking;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Placeholders extends EZPlaceholderHook {
    private Ranking plugin;

    public Placeholders(Ranking plugin) {
        super(plugin, "mctraps");
        this.plugin = plugin;
    }

    @Override
    public String onPlaceholderRequest(Player p, String id) {
        for (int i = 0; i < 17; i++) {
            if(id.equalsIgnoreCase("ranking" + i)) {
                if(new ArrayList<>(plugin.rankingTop.keySet()).size() >= i) {
                    return new ArrayList<>(plugin.rankingTop.keySet()).get(i - 1);
                } else {
                    return "";
                }
            }
            if(id.equalsIgnoreCase("rank" + i)) {
                if(new ArrayList<>(plugin.rankingTop.keySet()).size() >= i) {
                    return String.valueOf(new ArrayList<>(plugin.rankingTop.values()).get(i - 1));
                } else {
                    return "";
                }
            }
        }

        return null;
    }
}
