package me.miunapa.money.command.Sub;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.Account;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Top extends SubCommand {
    public Top() {
        super("top", "pamoney.top",
                ChatColor.GOLD + "/money top [起始]" + ChatColor.GRAY + "- 顯示金錢排行榜(以起始筆數向下顯示15筆");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (args.length != 1) {
            API.sendMessage(sender, this.getHelpMessage());
        } else {
            List<Account> accountList = API.getTop(0, 15);
            API.sendMessage(sender, "&7=============== &3金錢排行榜 &7===============", true);
            int rank = 1;
            for (Account account : accountList) {
                if (rank < 10) {
                    API.sendMessage(sender, "&a" + rank + ".  &e" + account.getName() + " &7- &c"
                            + API.formatAmountString(account.getAmount()) + " &7元", true);
                } else {
                    API.sendMessage(sender, "&a" + rank + ". &e" + account.getName() + " &7- &c"
                            + API.formatAmountString(account.getAmount()) + " &7元", true);
                }
                rank += 1;
            }
        }
        return false;
    }
}
