package me.miunapa.money.database;

import java.text.DecimalFormat;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import me.miunapa.money.Account;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class API {
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("PAMoney");
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

    public static boolean setup(String type) {
        if (type.toLowerCase().equals("mysql")) {
            db = new MySQL();
            if (db.getSetupStatus() == false) {
                return false;
            } else {
                return true;
            }
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

    public static void setBalanceByName(String name, double balance) {
        db.setBalanceByName(name, balance);
    }

    public static void setBalanceByUuid(String uuid, double balance) {
        db.setBalanceByUuid(uuid, balance);
    }

    public static boolean hasBalanceByName(String name) {
        return db.hasBalanceByName(name);
    }

    public static boolean hasBalanceByUuid(String uuid) {
        return db.hasBalanceByUuid(uuid);
    }

    public static String formatAmountString(double amount) {
        DecimalFormat df = new DecimalFormat("#,###.#");
        return df.format(amount);
    }

    public static double formatAmountdouble(double amount) {
        DecimalFormat df = new DecimalFormat("##.#");
        return Double.parseDouble(df.format(amount));
    }

    public static void withdraw(String name, double lessAmount) {
        double d = getBalanceByName(name);
        setBalanceByName(name, d - lessAmount);
    }

    public static void deposit(String name, double plusAmount) {
        double d = getBalanceByName(name);
        setBalanceByName(name, d + plusAmount);
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
}
