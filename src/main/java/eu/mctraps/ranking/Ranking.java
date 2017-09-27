package eu.mctraps.ranking;

import eu.mctraps.MCTrapsDisplayer.DisplayerAPI;
import eu.mctraps.MCTrapsDisplayer.MCTrapsDisplayer;
import eu.mctraps.ranking.commands.RankingCommand;
import eu.mctraps.ranking.listeners.PlayerDeathListener;
import eu.mctraps.ranking.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.LinkedHashMap;

public final class Ranking extends JavaPlugin {
    private Ranking pl = this;

    public FileConfiguration config;

    public LinkedHashMap<String, Integer> rankingTop;

    public String rTable;

    Connection connection;
    public Statement statement;
    private String host, database, username, password;
    private int port;

    public DisplayerAPI displayer;

    RankingCommand rankingCommand;
    PlayerDeathListener playerDeathListener;
    PlayerJoinListener playerJoinListener;

    @Override
    public void onEnable() {
        getLogger().info("MCTrapsRanking has been enabled");

        saveDefaultConfig();
        getDataFolder().mkdir();
        config = getConfig();

        host = config.getString("database.host");
        port = config.getInt("database.port");
        database = config.getString("database.database");
        username = config.getString("database.username");
        password = config.getString("database.password");
        rTable = config.getString("tables.ranking");

        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    openConnection();
                    statement = connection.createStatement();

                    ResultSet ranking = statement.executeQuery("SHOW TABLES LIKE '" + rTable + "'");
                    int count = 0;
                    while(ranking.next()) {
                        count++;
                    }

                    if(count == 0) {
                        statement.executeUpdate("CREATE TABLE ranking (id INT PRIMARY KEY NOT NULL AUTO_INCREMENT, player VARCHAR(50) NOT NULL, kills INT DEFAULT 0 NOT NULL, deaths INT NOT NULL DEFAULT 0 NOT NULL, rank INT DEFAULT 1000 NOT NULL)");
                        getLogger().info("Created table '" + rTable + "'");
                    }

                    rankingTop = RankingManager.top(pl);
                } catch(SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        r.runTaskAsynchronously(this);

        this.rankingCommand = new RankingCommand(this);

        this.playerDeathListener = new PlayerDeathListener(this);
        this.playerJoinListener = new PlayerJoinListener(this);

        this.displayer = new DisplayerAPI((MCTrapsDisplayer)this.getServer().getPluginManager().getPlugin("MCTrapsDisplayer"));

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).hook();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("MCTrapsShop has been disabled");
    }

    void openConnection() throws SQLException, ClassNotFoundException {
        if(connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if(connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.username, this.password);

            getLogger().info("Successfully connected to database. Hurrey!");
        }
    }

    public static String colorify(String s) {
        if(s != null) {
            return ChatColor.translateAlternateColorCodes('&', s);
        }

        return null;
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
