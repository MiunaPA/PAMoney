package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Deduct extends SubCommand {
    public Deduct() {
        super("deduct", "pamoney.deduct",
                ChatColor.RED + "/money deduct <ID> <amount> " + ChatColor.GRAY + "- 扣除他人的金錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length != 3) {
            API.sendMessage(sender, this.getHelpMessage());
            return false;
        }
        String deductName = args[1];
        if (!API.hasBalanceByName(deductName)) {
            API.sendMessage(sender, "&d此帳號不存在 無法扣除錢");
            return false;
        }
        try {
            double deductAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
            if (deductAmount <= 0.0) {
                API.sendMessage(sender, "&c金額不可小於或等於0");
                return true;
            }
            double resultAmount = API.withdraw(deductName, deductAmount, "管理員扣除");
            if (API.hasAmountByName(deductName, deductAmount)) {
                API.sendMessage(sender, "&a已將 &b" + deductName + " &a扣除了 &c" + deductAmount
                        + " &a元 &7(&2目前有 &c" + resultAmount + " &2元&7)");
            } else {
                API.sendMessage(sender, "&a已將 &b" + deductName + " &a扣除了 &c" + deductAmount
                        + " &a元 &7(&2目前有 &c" + resultAmount + " &2元&7)[已至負數]");
            }
        } catch (NumberFormatException e) {
            API.sendMessage(sender, "&c金額必須輸入數字(可有小數點)");
        }
        return false;
    }
}
