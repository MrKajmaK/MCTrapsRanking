package eu.mctraps.ranking;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

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

    public static boolean setRank(String player, double number, Ranking plugin) {
        try {
            if(number < 0) {
                number = 0;
            }
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

    public static int getPosition(String player, Ranking plugin) {
        try {
            ResultSet r = plugin.statement.executeQuery("SELECT position FROM (SELECT player, rank, @rownum := @rownum + 1 AS position FROM " + plugin.rTable + " JOIN (SELECT @rownum := 0) r ORDER BY rank DESC) x WHERE player = '" + player + "'");
            if(r.next()) {
                return r.getInt("position");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public static boolean createProfile(String player, Ranking plugin) {
        try {
            plugin.statement.executeUpdate("INSERT INTO " + plugin.rTable + " (player) VALUES ('" + player + "')");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public static LinkedHashMap<String, Integer> top(Ranking plugin) {
        LinkedHashMap<String, Integer> top = new LinkedHashMap<>();
        try {
            int limit = plugin.config.getInt("top");
            ResultSet r = plugin.statement.executeQuery("SELECT * FROM " + plugin.rTable + " ORDER BY rank DESC LIMIT " + limit);
            while(r.next()) {
                String name = r.getString("player");
                int rank = r.getInt("rank");
                top.put(name, rank);
            }
            return top;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
