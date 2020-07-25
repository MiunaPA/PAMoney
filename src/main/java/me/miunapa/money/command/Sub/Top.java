package me.miunapa.money.command.Sub;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.object.Account;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Top extends SubCommand {
    public Top() {
        super("top", "pamoney.top",
                ChatColor.RED + "/money top [起始名次]" + ChatColor.GRAY + " - 顯示金錢排行榜(以起始名次向下顯示15筆)");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length == 1) {
            List<Account> accountList = API.getTop(0, 15);
            API.sendMessage(sender, "&7=============== &3金錢排行榜 &7===============", true);
            if (accountList.size() == 0) {
                API.sendMessage(sender, "&d查詢不到任何人(可能是起始值太大)", true);
                return false;
            }
            for (Account account : accountList) {
                if (account.getRank() < 10) {
                    API.sendMessage(
                            sender, "&a" + account.getRank() + ".  &b" + account.getName()
                                    + " &7- &c" + API.formatAmountString(account.getAmount()),
                            true);
                } else {
                    API.sendMessage(
                            sender, "&a" + account.getRank() + ". &b" + account.getName()
                                    + " &7- &c" + API.formatAmountString(account.getAmount()),
                            true);
                }
            }
        } else if (args.length == 2) {
            try {
                Integer start = Integer.parseInt(args[1]);
                if (start < 1) {
                    start = 1;
                }
                List<Account> accountList = API.getTop(start - 1, 15);
                API.sendMessage(sender, "&7=============== &3金錢排行榜 &7===============", true);
                if (accountList.size() == 0) {
                    API.sendMessage(sender, "&d查詢不到任何人(可能是起始值太大)", true);
                    return false;
                }
                for (Account account : accountList) {
                    if (account.getRank() < 10) {
                        API.sendMessage(sender,
                                "&a" + account.getRank() + ".  &b" + account.getName() + " &7- &c"
                                        + API.formatAmountString(account.getAmount()) + " &7元",
                                true);
                    } else {
                        API.sendMessage(sender,
                                "&a" + account.getRank() + ". &b" + account.getName() + " &7- &c"
                                        + API.formatAmountString(account.getAmount()) + " &7元",
                                true);
                    }
                }
            } catch (NumberFormatException e) {
                API.sendMessage(sender, "&c起始名次必須輸入為數字");
            }
        }
        return false;
    }
}
