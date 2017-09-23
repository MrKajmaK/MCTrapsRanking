package eu.mctraps.ranking;

import java.sql.ResultSet;
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

    public static boolean setKills(String player, int number, Ranking plugin) {
        try {
            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET kills='" + number + "' WHERE player='" + player + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean setRank(String player, int number, Ranking plugin) {
        try {
            plugin.statement.executeUpdate("UPDATE " + plugin.rTable + " SET rank='" + number + "' WHERE player='" + player + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getDeaths(String player, Ranking plugin) {
        try {
            ResultSet r = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " WHERE player='" + player + "'");
            if(r.next()) {
                return r.getInt("deaths");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public static int getKills(String player, Ranking plugin) {
        try {
            ResultSet r = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " WHERE player='" + player + "'");
            if(r.next()) {
                return r.getInt("kills");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public static int getRank(String player, Ranking plugin) {
        try {
            ResultSet r = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " WHERE player='" + player + "'");
            if(r.next()) {
                return r.getInt("rank");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public static boolean exists(String player, Ranking plugin) {
        try {
            ResultSet r = plugin.statement.executeQuery("SELECT COUNT(*) FROM " + plugin.rTable + " WHERE player='" + player + "'");
            int count = 0;
            while(r.next()) {
                count = r.getInt(1);
            }

            if(count > 0) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
