package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;

public class Reconnect extends SubCommand {
    public Reconnect() {
        super("reconnect", "pamoney.reconnect",
                ChatColor.RED + "/money reconnect " + ChatColor.GRAY + "- 重新連線至資料庫");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        API.sendMessage(sender, ChatColor.YELLOW + "正在重新連線中");
        API.reconnect();
        API.sendMessage(sender, ChatColor.GREEN + "重新連線成功!");
        return false;
    }
}
