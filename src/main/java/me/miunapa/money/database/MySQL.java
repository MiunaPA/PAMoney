package me.miunapa.money.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;
import me.miunapa.money.object.Account;
import me.miunapa.money.object.Record;
import me.miunapa.money.Main;

public class MySQL implements Listener, Database {
    private Connection connection;
    private String host, database, username, password;
    private int port;
    Main plugin = Main.getPlugin(Main.class);
    FileConfiguration config = plugin.getConfig();
    boolean setupStatus = false;
    double initMoney = config.getDouble("init_money");

    public MySQL() {
        host = config.getString("MySQL.connect.host");
        port = config.getInt("MySQL.connect.port");
        database = config.getString("MySQL.connect.database");
        username = config.getString("MySQL.connect.username");
        password = config.getString("MySQL.connect.password");
        try {
            openConnection();
            setupStatus = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, plugin);
    }

    void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection =
                    DriverManager.getConnection(
                            "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database
                                    + "?useSSL=false&autoReconnect=true",
                            this.username, this.password);
        }
    }

    public void reconnect() {
        try {
            plugin.getLogger().info("reconnecting...");
            connection.close();
            connection = null;
            openConnection();
            plugin.getLogger().info("reconnected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createTable() {
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Statement statement = connection.createStatement();
                    String createIdSQL = "CREATE TABLE IF NOT EXISTS " + database + ".pamoney ("
                            + "uuid CHAR(36) PRIMARY KEY," + "name CHAR(16) NOT NULL,"
                            + "balance DOUBLE);";
                    statement.execute(createIdSQL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    Statement statement = connection.createStatement();
                    String createRecordSQL = "CREATE TABLE IF NOT EXISTS " + database
                            + ".pamoney_record (" + "time timestamp(3) DEFAULT NOW(3),"
                            + "uuid CHAR(36) NOT NULL," + "vary DOUBLE," + "balance DOUBLE,"
                            + "remark VARCHAR(64),"
                            + "FOREIGN KEY(uuid) REFERENCES pamoney(uuid));";
                    statement.execute(createRecordSQL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        r.runTaskAsynchronously(plugin);
    }

    @EventHandler
    void onPlayerLoginEvent(PlayerLoginEvent event) {
        updatePlayerData(event.getPlayer());
    }

    void updatePlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement psSelect = connection.prepareStatement(
                            "SELECT * FROM " + database + ".pamoney where uuid=?;");
                    psSelect.setString(1, uuid);
                    ResultSet result = psSelect.executeQuery();
                    if (result.next() == false) {
                        PreparedStatement psInsert = connection.prepareStatement("INSERT INTO "
                                + database + ".pamoney (uuid, name, balance) VALUES (?, ?, ?);");
                        psInsert.setString(1, uuid);
                        psInsert.setString(2, name);
                        psInsert.setDouble(3, 0);
                        psInsert.executeUpdate();
                        API.deposit(name, initMoney, "新玩家初始化");
                    } else {
                        result.beforeFirst();
                        result.next();
                        String sqlName = result.getString("name");
                        double amount = getBalanceByUuid(uuid);
                        if (!sqlName.equals(name)) {
                            PreparedStatement psUpdate = connection.prepareStatement(
                                    "UPDATE " + database + ".pamoney set name=? where uuid=?;");
                            addRecord(uuid, 0, amount, "ID更改 " + sqlName + " --> " + name);
                            psUpdate.setString(1, name);
                            psUpdate.setString(2, uuid);
                            psUpdate.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        r.runTaskAsynchronously(plugin);
    }

    public void initData() {
        createTable();
    }

    public boolean getSetupStatus() {
        return setupStatus;
    }

    public String getUuidByName(String name) {
        if (hasBalanceByName(name)) {
            try {
                PreparedStatement psSelect = connection.prepareStatement(
                        "SELECT uuid FROM " + database + ".pamoney where name = ?;");
                psSelect.setString(1, name);
                ResultSet result = psSelect.executeQuery();
                result.next();
                return result.getString("uuid");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String getNameByUuid(String uuid) {
        if (hasBalanceByUuid(uuid)) {
            try {
                PreparedStatement psSelect = connection.prepareStatement(
                        "SELECT name FROM " + database + ".pamoney where uuid = ?;");
                psSelect.setString(1, uuid);
                ResultSet result = psSelect.executeQuery();
                result.next();
                return result.getString("name");
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean hasBalanceByName(String name) {
        try {
            PreparedStatement psSelectCount = connection.prepareStatement(
                    "SELECT COUNT(*) FROM " + database + ".pamoney WHERE name=?;");
            psSelectCount.setString(1, name);
            ResultSet resultCount = psSelectCount.executeQuery();
            resultCount.next();
            Integer resultCountInt = resultCount.getInt("COUNT(*)");
            if (resultCountInt == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasBalanceByUuid(String uuid) {
        try {
            PreparedStatement psSelectCount = connection.prepareStatement(
                    "SELECT COUNT(*) FROM " + database + ".pamoney WHERE uuid=?;");
            psSelectCount.setString(1, uuid);
            ResultSet resultCount = psSelectCount.executeQuery();
            resultCount.next();
            Integer resultCountInt = resultCount.getInt("COUNT(*)");
            if (resultCountInt == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Double getBalanceByName(String name) {
        try {
            if (hasBalanceByName(name)) {
                PreparedStatement psSelect = connection
                        .prepareStatement("SELECT * FROM " + database + ".pamoney WHERE name=?;");
                psSelect.setString(1, name);
                ResultSet result = psSelect.executeQuery();
                result.next();
                return result.getDouble("balance");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double getBalanceByUuid(String uuid) {
        try {
            if (hasBalanceByUuid(uuid)) {
                PreparedStatement psSelect = connection
                        .prepareStatement("SELECT * FROM " + database + ".pamoney WHERE uuid=?;");
                psSelect.setString(1, uuid);
                ResultSet result = psSelect.executeQuery();
                result.next();
                return result.getDouble("balance");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setBalanceByName(String name, double balance) {
        try {
            if (hasBalanceByName(name)) {
                PreparedStatement psUpdate = connection.prepareStatement(
                        "UPDATE " + database + ".pamoney set balance=? where name=?;");
                psUpdate.setDouble(1, balance);
                psUpdate.setString(2, name);
                psUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBalanceByUuid(String uuid, double balance) {
        try {
            if (hasBalanceByUuid(uuid)) {
                PreparedStatement psUpdate = connection.prepareStatement(
                        "UPDATE " + database + ".pamoney set balance=? where uuid=?;");
                psUpdate.setDouble(1, balance);
                psUpdate.setString(2, uuid);
                psUpdate.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Account> getTop(int start, int count) {
        try {
            PreparedStatement psSelect = connection.prepareStatement(
                    "SELECT uuid,name,balance,RANK() OVER (ORDER BY balance DESC) AS top_rank FROM "
                            + database + ".pamoney LIMIT ?,?;");
            psSelect.setInt(1, start);
            psSelect.setInt(2, count);
            ResultSet result = psSelect.executeQuery();
            List<Account> resultList = new ArrayList<Account>();
            while (result.next()) {
                String uuid = result.getString("uuid");
                String name = result.getString("name");
                double amount = result.getDouble("balance");
                int rank = result.getInt("top_rank");
                resultList.add(new Account(uuid, name, amount, rank));
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRecord(String uuid, double vary, double balance, String remark) {
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    PreparedStatement psInsert = connection.prepareStatement("INSERT INTO "
                            + database
                            + ".pamoney_record (uuid, vary, balance,remark) VALUES (?, ?, ?, ?);");
                    psInsert.setString(1, uuid);
                    psInsert.setDouble(2, vary);
                    psInsert.setDouble(3, balance);
                    psInsert.setString(4, remark);
                    psInsert.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        r.runTaskAsynchronously(plugin);
    }

    public List<Record> getRecord(String uuid, int start, int count) {
        try {
            PreparedStatement psSelect = connection.prepareStatement(
                    "SELECT time,vary,balance,remark,RANK() OVER (ORDER BY time DESC) AS no FROM "
                            + database + ".pamoney_record WHERE uuid=? LIMIT ?,?;");
            psSelect.setString(1, uuid);
            psSelect.setInt(2, start);
            psSelect.setInt(3, count);
            ResultSet result = psSelect.executeQuery();
            List<Record> recordList = new ArrayList<Record>();
            while (result.next()) {
                recordList.add(new Record(result.getInt("no"), result.getTimestamp("time"),
                        result.getDouble("vary"), result.getDouble("balance"),
                        result.getString("remark")));
            }
            Collections.reverse(recordList);
            return recordList;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
