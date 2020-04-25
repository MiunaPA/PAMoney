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
        String type = config.getString("type").toLowerCase();
        if (type.equals("mysql")) {
            getLogger().info("資料儲存方式 : MySQL");
            if (!API.setup(type)) {
                getLogger().info("MySQL 連線錯誤! ");
                this.onDisable();
                return;
            } else {
                API.initData();
            }
        } else if (type.equals("yml")) {
            getLogger().info("資料儲存方式 : yml");
        }
        init();
        getLogger().info("PAMoney Starting!  Author:MiunaPA");
        API.setConfig(config);
        if (API.setupEconomy()) {
            getLogger().info("PA Money");
        }
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
