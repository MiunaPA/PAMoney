package me.miunapa.money;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import me.miunapa.money.database.API;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class VaultHandler implements Economy {

    private final String name = "PA-Money";
    private Main plugin;
    FileConfiguration config = plugin.getConfig();


    public VaultHandler(Main plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Vault support enabled.");;
    }

    @Override
    public boolean isEnabled() {
        return plugin != null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return API.formatAmount(amount);
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
            Double d = API.getBalanceByName(playerName);
            if (has(playerName, amount)) {
                API.withdraw(name, amount);
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
            Double limit = config.getDouble("limit_money");
            Double d = API.getBalanceByName(playerName);
            if ((d + amount) <= limit) {
                API.deposit(name, amount);
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
