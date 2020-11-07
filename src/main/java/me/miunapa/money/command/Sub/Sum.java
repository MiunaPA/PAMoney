package me.miunapa.money.command.Sub;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Sum extends SubCommand {
    public Sum() {
        super("sum", "pamoney.sum",
                ChatColor.RED + "/money sum [最小總額] " + ChatColor.GRAY + "- 查詢金錢發行總額");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1 && args.length != 2) {
            API.sendMessage(sender, this.getHelpMessage());
            return false;
        }
        double minBalance = 0.0;
        try {
            if (args.length == 2) {
                minBalance = API.formatAmountdouble(Double.parseDouble(args[1]));
            }
        } catch (NumberFormatException e) {
            API.sendMessage(sender, "&c最小總額金額必須輸入整數數字");
            return false;
        }
        String resultSumText = API.formatAmountString(API.getSumBalance(minBalance));
        if (minBalance < 1) {
            API.sendMessage(sender, "&a目前貨幣總額&7 : &c" + resultSumText + " &3"
                    + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        } else {
            API.sendMessage(sender, "&a目前貨幣總額&7 : &c" + resultSumText + " &7(排除<" + minBalance
                    + ") &3" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        }
        return false;
    }
}
