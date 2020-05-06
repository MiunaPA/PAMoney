package me.miunapa.money;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import me.miunapa.money.database.API;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultHandler implements Economy {
    private final String pluginName = "PA-Money";
    private Main plugin;
    FileConfiguration config;

    public VaultHandler(Main plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new EconomyServerListener(), plugin);
        plugin.getLogger().info("Vault support enabled.");;
    }

    public class EconomyServerListener implements Listener {
        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginEnable(PluginEnableEvent event) {
            if (plugin == null) {
                Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PAMoney");
                if (plugin != null && plugin.isEnabled()) {
                    VaultHandler.this.plugin = (Main) plugin;
                    VaultHandler.this.plugin.getLogger().info("Vault support enabled.");
                }
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        public void onPluginDisable(PluginDisableEvent event) {
            if (plugin != null) {
                if (event.getPlugin().getDescription().getName().equals(pluginName)) {
                    plugin = null;
                    Bukkit.getLogger().info("Vault support disabled.");
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return plugin != null;
    }

    @Override
    public String getName() {
        return pluginName;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 1;
    }

    @Override
    public String format(double amount) {
        return API.formatAmountString(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "元";
    }

    @Override
    public String currencyNameSingular() {
        return "元";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return API.hasBalanceByName(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return API.hasBalanceByUuid(player.getUniqueId().toString());
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return API.hasBalanceByName(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return API.hasBalanceByUuid(player.getUniqueId().toString());
    }

    @Override
    public double getBalance(String playerName) {
        return API.getBalanceByName(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return API.getBalanceByUuid(player.getUniqueId().toString());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return API.getBalanceByName(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return API.getBalanceByUuid(player.getUniqueId().toString());
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double amount) {
        return getBalance(offlinePlayer) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    EconomyResponse withdraw(String playerName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "不能使用負數進行提款");
        }
        if (!API.hasBalanceByName(playerName)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "找不到此帳號");
        } else {
            double d = API.getBalanceByName(playerName);
            if (has(playerName, amount)) {
                API.withdraw(playerName, amount, "Vault 提款交易");
                return new EconomyResponse(amount, d - amount, ResponseType.SUCCESS, "");
            } else {
                return new EconomyResponse(0, d, ResponseType.FAILURE, "金額不足");
            }
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdraw(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdraw(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdraw(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdraw(player.getName(), amount);
    }

    EconomyResponse deposit(String playerName, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "不能使用負數進行提款");
        }
        if (!API.hasBalanceByName(playerName)) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "找不到此帳號");
        } else {
            double limit = config.getDouble("limit_money");
            double d = API.getBalanceByName(playerName);
            if ((d + amount) <= limit) {
                API.deposit(playerName, amount, "Vault 存款交易");
                return new EconomyResponse(amount, d + amount, ResponseType.SUCCESS, "");
            } else {
                return new EconomyResponse(0, d, ResponseType.FAILURE, "金額超過上限");
            }
        }
    }


    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return deposit(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return deposit(player.getName(), amount);

    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return deposit(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return deposit(player.getName(), amount);

    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED,
                "PA-Money does not support bank accounts!");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<String>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
