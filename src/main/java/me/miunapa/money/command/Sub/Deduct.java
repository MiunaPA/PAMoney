package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Deduct extends SubCommand {
    public Deduct() {
        super("deduct", "pamoney.deduct",
                ChatColor.GOLD + "/money deduct <ID> <amount> " + ChatColor.GRAY + "- 扣除他人的金錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length != 3) {
            API.sendMessage(sender, this.getHelpMessage());
        } else {
            String deductName = args[1];
            if (API.hasBalanceByName(deductName)) {
                try {
                    double deductAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
                    if (API.hasAmountByName(deductName, deductAmount)) {
                        API.withdraw(deductName, deductAmount);
                        API.sendMessage(sender,
                                "&a已將 &e" + deductName + " &a扣除了 &c" + deductAmount
                                        + " &a元 &7(&2目前有 &c" + API.getBalanceByName(deductName)
                                        + " &2元&7)");
                    } else {
                        API.sendMessage(sender, "&e" + deductName + "&d 的金額不足 無法執行");
                    }
                } catch (NumberFormatException e) {
                    API.sendMessage(sender, "金額必須輸入數字(可有小數點)");
                }
            } else {
                API.sendMessage(sender, "&d此帳號不存在");
            }
        }
        return false;
    }
}
