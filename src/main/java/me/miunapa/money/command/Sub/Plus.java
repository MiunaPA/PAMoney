package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Plus extends SubCommand {
    public Plus() {
        super("plus", "pamoney.plus",
                ChatColor.GOLD + "/money plus <ID> <amount> " + ChatColor.GRAY + "- 增加他人的金錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length != 3) {
            API.sendMessage(sender, this.getHelpMessage());
        } else {
            String plusName = args[1];
            if (API.hasBalanceByName(plusName)) {
                Double plusAmount = Double.parseDouble(args[2]);
                API.deposit(plusName, plusAmount);
                API.sendMessage(sender, "&a已將 &e" + plusName + " &a增加了 &c" + plusAmount
                        + " &a元 &7(&2目前有 &c" + API.getBalanceByName(plusName) + " &2元&7)");
            } else {
                API.sendMessage(sender, "&d此帳號不存在");
            }
        }
        return false;
    }
}
