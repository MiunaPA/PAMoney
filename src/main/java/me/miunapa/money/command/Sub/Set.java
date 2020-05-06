package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Set extends SubCommand {
    public Set() {
        super("set", "pamoney.set",
                ChatColor.RED + "/money set <ID> <amount> " + ChatColor.GRAY + "- 設定他人的金錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length != 3) {
            API.sendMessage(sender, this.getHelpMessage());
            return false;
        }
        String setName = args[1];
        if (!API.hasBalanceByName(setName)) {
            API.sendMessage(sender, "&d此帳號不存在");
            return false;
        }
        try {
            double setAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
            if (setAmount < API.getConfig().getDouble("limit_money")) {
                double originalAmount = API.getBalanceByName(setName);
                if (originalAmount > setAmount) {
                    API.withdraw(setName, originalAmount - setAmount);
                    API.sendMessage(sender,
                            "&a已將 &b" + setName + " &a設定為 &c" + setAmount + " &a元&7(扣除)");
                } else if (originalAmount < setAmount) {
                    API.deposit(setName, originalAmount + setAmount);
                    API.sendMessage(sender,
                            "&a已將 &b" + setName + " &a設定為 &c" + setAmount + " &a元&7(增加)");
                } else {
                    API.sendMessage(sender,
                            "&b" + setName + " 的餘額已經是  &c" + setAmount + " &a元&7(不更動)");
                }
            } else {
                API.sendMessage(sender, "&b" + setName + "&d 設定後的錢會超過可擁有的上限 無法執行");
            }
        } catch (NumberFormatException e) {
            API.sendMessage(sender, "&c金額必須輸入數字(可有小數點)");
        }
        return false;
    }
}
