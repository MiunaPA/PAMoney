package me.miunapa.money.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import me.miunapa.money.command.Sub.*;
import me.miunapa.money.database.API;

public class RootCommand implements CommandExecutor {
    List<SubCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if (args.length < 1) {
                if (sender instanceof Player) {
                    getBalance(sender, ((Player) sender).getName());
                } else {
                    API.sendMessage(sender, "args >1 null console");
                }
            } else {
                SubCommand sub = getCommand(args[0]);
                if (sub != null) {
                    sub.onCommand(sender, command, label, args);
                } else {
                    if (args.length == 1) {
                        getBalance(sender, args[0]);
                    } else {
                        API.sendMessage(sender, "args >1 null");
                    }
                }
            }
        }
        return true;
    }

    SubCommand getCommand(String name) {
        for (SubCommand command : commands) {
            if (command.getName().equals(name)) {
                return command;
            }
        }
        return null;
    }

    public RootCommand() {
        Bukkit.getPluginCommand("money").setExecutor(this);
        commands = new ArrayList<SubCommand>();
        commands.add(new Plus());
    }

    void getBalance(CommandSender sender, String name) {
        if (API.hasBalanceByName(name)) {
            Double d = API.getBalanceByName(name);
            API.sendMessage(sender, "&e" + name + " &7目前有 &c" + API.formatAmount(d) + " &7元");
        } else {
            API.sendMessage(sender, "&d此帳號不存在");
        }
    }
}
