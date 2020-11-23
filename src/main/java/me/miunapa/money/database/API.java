package me.miunapa.money.database;

import java.text.DecimalFormat;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import me.miunapa.money.object.Account;
import me.miunapa.money.object.Record;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class API {
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("PA_Money");
    private static Database db = null;
    private static String prefix =
            ChatColor.GRAY + "[" + ChatColor.GOLD + "PA-Money" + ChatColor.GRAY + "] ";
    private static Economy econ = null;
    private static FileConfiguration config = null;

    public static boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp =
                plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static void setConfig(FileConfiguration sendConfig) {
        config = sendConfig;
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static boolean setup() {
        db = new MySQL();
        if (db.getSetupStatus() == false) {
            return false;
        } else {
            return true;
        }
    }

    public static void initData() {
        db.initData();
    }

    public static void sendMessage(CommandSender sender, String message, boolean noPrefix) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static double getBalanceByName(String name) {
        return db.getBalanceByName(name);
    }

    public static double getBalanceByUuid(String uuid) {
        return db.getBalanceByUuid(uuid);
    }

    public static double setBalanceByName(String name, double vary, String remark) {
        return setBalance(getUuidByName(name), vary, remark);
    }

    /**
     * 調整玩家的金錢(與資料庫交互)
     * 
     * @param uuid   玩家的UUID
     * @param vary   玩家要修改的金錢量
     * @param remark 備註
     */
    static double setBalance(String uuid, double vary, String remark) {
        double adjustAmount = getBalanceByUuid(uuid) + vary;
        db.setBalanceByUuid(uuid, adjustAmount);
        db.addRecord(uuid, vary, adjustAmount, remark);
        return adjustAmount;
    }

    public static boolean hasBalanceByName(String name) {
        return db.hasBalanceByName(name);
    }

    public static boolean hasBalanceByUuid(String uuid) {
        return db.hasBalanceByUuid(uuid);
    }

    public static String formatAmountString(double amount) {
        DecimalFormat df = new DecimalFormat("#,###.#");
        return config.getString("format").replace("<amount>", df.format(amount));
    }

    public static double formatAmountdouble(double amount) {
        DecimalFormat df = new DecimalFormat("##.#");
        return Double.parseDouble(df.format(amount));
    }

    /**
     * 減少錢(提款)
     * 
     * @param name       玩家的Name
     * @param lessAmount 要扣除多少錢至該玩家
     */
    public static double withdraw(String name, double lessAmount) {
        return withdraw(name, lessAmount, "");
    }

    /**
     * 減少錢(提款)
     * 
     * @param name       玩家的Name
     * @param lessAmount 要扣除多少錢至該玩家
     * @param remark     備註
     */
    public static double withdraw(String name, double lessAmount, String remark) {
        return setBalanceByName(name, -lessAmount, remark);
    }

    /**
     * 增加錢(存款)
     * 
     * @param name       玩家的Name
     * @param plusAmount 要增加多少錢至該玩家
     * 
     */
    public static double deposit(String name, double plusAmount) {
        return deposit(name, plusAmount, "");
    }

    /**
     * 增加錢(存款)
     * 
     * @param name       玩家的Name
     * @param plusAmount 要增加多少錢至該玩家
     * @param remark     備註
     * 
     */
    public static double deposit(String name, double plusAmount, String remark) {
        return setBalanceByName(name, plusAmount, remark);
    }

    public static boolean hasAmountByName(String name, double amount) {
        if (getBalanceByName(name) >= amount) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasAmountByUuid(String uuid, double amount) {
        if (getBalanceByUuid(uuid) >= amount) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Account> getTop(int start, int count) {
        return db.getTop(start, count);
    }

    public static String getUuidByName(String name) {
        return db.getUuidByName(name);
    }

    public static String getNameByUuid(String uuid) {
        return db.getNameByUuid(uuid);
    }

    public static List<Record> getRecordList(String uuid, int start, int count) {
        return db.getRecord(uuid, start, count);
    }

    public static Double getSumBalance(double minBalance) {
        return db.getSumBalance(minBalance);
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void reconnect() {
        db.reconnect();
    }

    public static void disconnect() {
        db.disconnect();
    }
}
