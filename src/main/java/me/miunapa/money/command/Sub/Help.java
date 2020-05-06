package me.miunapa.money.command.Sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.miunapa.money.command.RootCommand;
import me.miunapa.money.command.SubCommand;
import net.md_5.bungee.api.ChatColor;

public class Help extends SubCommand {
    public Help() {
        super("help", "pamoney.help",
                ChatColor.RED + "/money help " + ChatColor.GRAY + "- 查看經濟插件指令幫助");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        RootCommand.sendHelpMessage(sender);
        return false;
    }
}
