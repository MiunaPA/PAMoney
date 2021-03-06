package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Grant extends SubCommand {

    public Grant() {
        super("grant", "pamoney.grant",
                ChatColor.RED + "/money grant <ID> <amount> " + ChatColor.GRAY + "- 增加他人的金錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length != 3) {
            API.sendMessage(sender, this.getHelpMessage());
            return false;
        }
        String grantName = args[1];
        if (!API.hasBalanceByName(grantName)) {
            API.sendMessage(sender, "&d此帳號不存在 無法增加錢");
            return false;
        }
        try {
            double grantAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
            if (grantAmount <= 0.0) {
                API.sendMessage(sender, "&c金額不可小於或等於0");
                return true;
            }
            if ((API.getBalanceByName(grantName) + grantAmount) < API.getConfig()
                    .getDouble("limit_money")) {
                double resultAmount = API.deposit(grantName, grantAmount, "管理員增加");
                API.sendMessage(sender, "&a已將 &b" + grantName + " &a增加了 &c" + grantAmount
                        + " &a元 &7(&2目前有 &c" + resultAmount + " &2元&7)");
            } else {
                API.sendMessage(sender, "&b" + grantName + "&d 增加後的錢會超過可擁有的上限 無法執行");
            }
        } catch (NumberFormatException e) {
            API.sendMessage(sender, "&c金額必須輸入數字(可有小數點)");
        }
        return false;
    }
}
