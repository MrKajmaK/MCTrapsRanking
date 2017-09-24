package eu.mctraps.ranking;

import eu.mctraps.ranking.commands.RankingCommand;
import eu.mctraps.ranking.listeners.PlayerDeathListener;
import eu.mctraps.ranking.listeners.PlayerJoinListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public final class Ranking extends JavaPlugin {
    public FileConfiguration config;

    public String rTable;

    Connection connection;
    public Statement statement;
    private String host, database, username, password;
    private int port;

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
                } catch(SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };
        r.runTaskAsynchronously(this);

        this.rankingCommand = new RankingCommand(this);

        this.playerDeathListener = new PlayerDeathListener(this);
        this.playerJoinListener = new PlayerJoinListener(this);
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

//    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
//        if (cmd.getName().equalsIgnoreCase("ranking")) {
//            if (args.length == 0) {
//                if (sender instanceof Player) {
//                    Player p = (Player) sender;
//                    String name = p.getName();
//                    int kills = RankingManager.getKills(name, this);
//                    int deaths = RankingManager.getDeaths(name, this);
//                    int rank = RankingManager.getRank(name, this);
//                    float kd = kills / deaths;
//
//                    sender.sendMessage("§8§m--------§8§l« §9§lTWOJE STATYSTYKI §8§l»§8§m--------");
//                    sender.sendMessage("§8● §7Ranking §8§l» §6" + rank);
//                    sender.sendMessage("§8● §7Zabojstwa §8§l» §6" + kills);
//                    sender.sendMessage("§8● §7Smierci §8§l» §6" + deaths);
//                    sender.sendMessage("§8● §7K/D §8§l» §6" + kd);
//                    sender.sendMessage("§8§m-----------------------------------------");
//                } else {
//                    sender.sendMessage("§cPoprawne uzycie: §7/" + label + " §c<nick>");
//                }
//            } else {
//                if (args[0].equalsIgnoreCase("top")) {
//                    HashMap<String, Integer> top = RankingManager.top(this);
//
//                    int i = 1;
//
//                    sender.sendMessage("§8§m--------§8§l« §9§lTOP 10 GRACZY §8§l»§8§m--------");
//                    for (HashMap.Entry<String, Integer> entry : top.entrySet()) {
//                        if (entry.getKey().equalsIgnoreCase(sender.getName())) {
//                            sender.sendMessage("§8" + i + ". §7§l" + entry.getKey() + " §8(§6" + entry.getValue() + "§8)");
//                        } else {
//                            sender.sendMessage("§8" + i + ". §7" + entry.getKey() + " §8(§6" + entry.getValue() + "§8)");
//                        }
//                        i++;
//                    }
//                    sender.sendMessage("§8§m-----------------------------------------");
//                    if (top.containsKey(sender.getName()) && (sender instanceof Player)) {
//                        int position = RankingManager.getPosition(sender.getName(), this);
//                        int rank = RankingManager.getRank(sender.getName(), this);
//
//                        sender.sendMessage("§8" + position + ". §7" + sender.getName() + " §8(§6" + rank + "§8)");
//                    }
//                } else {
//                    if (RankingManager.exists(args[0], this)) {
//                        String name = args[0];
//                        int kills = RankingManager.getKills(name, this);
//                        int deaths = RankingManager.getDeaths(name, this);
//                        int rank = RankingManager.getRank(name, this);
//                        float kd = kills / deaths;
//
//                        sender.sendMessage("§8§m--------§8§l« §9§lSTATYSTYKI§9: §7" + name + " §8§l»§8§m--------");
//                        sender.sendMessage("§8● §7Ranking §8§l» §6" + rank);
//                        sender.sendMessage("§8● §7Zabojstwa §8§l» §6" + kills);
//                        sender.sendMessage("§8● §7Smierci §8§l» §6" + deaths);
//                        sender.sendMessage("§8● §7K/D §8§l» §6" + kd);
//                        sender.sendMessage("§8§m-----------------------------------------");
//                    } else {
//                        sender.sendMessage("§cBlad: §4Taki gracz nie istnieje");
//                    }
//                }
//            }
//            return false;
//        }
//        return false;
//    }
}
