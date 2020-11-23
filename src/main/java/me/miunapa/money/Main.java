package me.miunapa.money;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import me.miunapa.money.command.RootCommand;
import me.miunapa.money.database.API;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        configSet();
        if (!API.setup()) {
            getLogger().info("MySQL 連線錯誤! ");
            this.onDisable();
            return;
        } else {
            API.initData();
        }
        init();
        API.setConfig(config);
        if (API.setupEconomy()) {
            getLogger().info("PA_Money Starting!  Author:MiunaPA");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("PA_Money Stop!  Author:MiunaPA");
    }

    void init() {
        setupVault();
        new RootCommand();
    }

    void configSet() {
        config.options().copyDefaults(true);
        saveConfig();
    }

    public String getServerName() {
        return config.getString("server_name");
    }

    void setupVault() {
        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault == null) {
            return;
        }
        getServer().getServicesManager().register(Economy.class, new VaultHandler(this), this,
                ServicePriority.Highest);
    }
}
