package me.miunapa.money.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class API {
    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("PAMoney");
    private static FileConfiguration config = plugin.getConfig();
    private static Database db = null;
    private static String prefix = ChatColor.LIGHT_PURPLE + "[" + ChatColor.AQUA + "PA-Money"
            + ChatColor.LIGHT_PURPLE + "] ";
    private static Economy econ = null;


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
}
