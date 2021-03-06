package me.miunapa.money.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import me.miunapa.money.command.Sub.*;
import me.miunapa.money.database.API;

public class RootCommand implements CommandExecutor, TabCompleter {
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
        API.sendMessage(sender, "&7=============== &e金錢插件 指令幫助 &7===============");
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
        commands.add(new Lookup());
        commands.add(new Reconnect());
        commands.add(new Sum());
    }

    void getBalance(CommandSender sender, String name) {
        if (API.hasBalanceByName(name)) {
            double d = API.getBalanceByName(name);
            API.sendMessage(sender, "&b" + name + " &7目前有 &c" + API.formatAmountString(d));
        } else {
            API.sendMessage(sender, "&d此帳號不存在 無法查詢餘額");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias,
            String[] args) {
        if (command.getName().equalsIgnoreCase("money") && args.length == 1) {
            List<String> subList = new ArrayList<String>();
            for (SubCommand sub : commands) {
                if (sender instanceof Player) {
                    if (((Player) sender).hasPermission(sub.getPermission())) {
                        subList.add(sub.getName());
                    }
                } else {
                    subList.add(sub.getName());
                }
            }
            return subList;
        }
        return null;
    }
}
