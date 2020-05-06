package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Pay extends SubCommand {
    public Pay() {
        super("pay", "pamoney.pay",
                ChatColor.RED + "/money pay <ID> <amount> " + ChatColor.GRAY + "- 給別人錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (!(sender instanceof Player)) {
            API.sendMessage(sender, "&e給錢的指令只有玩家才能使用");
            return false;
        }
        if (args.length != 3) {
            API.sendMessage(sender, this.getHelpMessage());
            return false;
        }
        String payName = args[1];
        if (sender.getName().equals(payName)) {
            API.sendMessage(sender, "&c不能給自己錢");
            return false;
        }
        if (!API.hasBalanceByName(payName)) {
            API.sendMessage(sender, "&d此帳號不存在");
            return false;
        }
        try {
            double payAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
            if ((API.getBalanceByName(payName) + payAmount) < API.getConfig()
                    .getDouble("limit_money")) {
                API.withdraw(sender.getName(), payAmount, "轉帳給:" + payName);
                API.deposit(payName, payAmount, "收到轉帳:" + sender.getName());
                API.sendMessage(sender, "&a已將 &c" + payAmount + " &a元 轉帳給 &b" + payName);
            } else {
                API.sendMessage(sender, "&b" + payName + "&d 擁有的錢會超過上限 無法執行");
            }
        } catch (NumberFormatException e) {
            API.sendMessage(sender, "&c金額必須輸入數字(可有小數點)");
        }
        return false;
    }
}
