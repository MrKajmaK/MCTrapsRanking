package eu.mctraps.ranking;

import java.sql.SQLException;

public class RankingManager {
    public static boolean setDeaths(String player, int number, Ranking plugin) {
        try {
            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET deaths='" + number + "' WHERE player='" + player + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
