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
    static List<SubCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            if (args.length < 1) {
                if (sender instanceof Player) {
                    getBalance(sender, ((Player) sender).getName());
                } else {
                    API.sendMessage(sender, "&c非玩家無法查詢自己的餘額");
                }
            } else {
                SubCommand sub = getCommand(args[0]);
                if (sub != null) {
                    if (sender instanceof Player) {
                        if (((Player) sender).hasPermission(sub.getPermission())) {
                            sub.onCommand(sender, command, label, args);
                        } else {
                            API.sendMessage(sender, "&c你沒有權限使用此指令");
                        }
                    } else {
                        sub.onCommand(sender, command, label, args);
                    }
                } else {
                    if (args.length == 1) {
                        getBalance(sender, args[0]);
                    } else {
                        sendHelpMessage(sender);
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

    public static void sendHelpMessage(CommandSender sender) {
        for (SubCommand command : commands) {
            if (sender instanceof Player) {
                if (((Player) sender).hasPermission(command.getPermission())) {
                    API.sendMessage(sender, command.getHelpMessage());
                }
            } else {
                API.sendMessage(sender, command.getHelpMessage());
            }
        }
    }

    public RootCommand() {
        Bukkit.getPluginCommand("money").setExecutor(this);
        commands = new ArrayList<SubCommand>();
        commands.add(new Grant());
        commands.add(new Deduct());
        commands.add(new Set());
        commands.add(new Top());
        commands.add(new Pay());
        commands.add(new Help());
    }

    void getBalance(CommandSender sender, String name) {
        if (API.hasBalanceByName(name)) {
            double d = API.getBalanceByName(name);
            API.sendMessage(sender, "&b" + name + " &7目前有 &c" + API.formatAmountString(d) + " &7元");
        } else {
            API.sendMessage(sender, "&d此帳號不存在");
        }
    }
}
