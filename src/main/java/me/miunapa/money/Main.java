package me.miunapa.money;

import org.bukkit.plugin.java.JavaPlugin;
import me.miunapa.money.database.API;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        if (API.setupEconomy()) {
            getLogger().info("PA Money");
        }
    }
}
