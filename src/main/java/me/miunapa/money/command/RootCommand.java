package me.miunapa.money.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import me.miunapa.money.database.API;

public class RootCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            if (args.length >= 1) {
                String type = args[0];
                if (type.equals("add")) {
                    Double b = API.getBalanceByUuid(uuid);
                    API.setBalanceByUuid(uuid, b + 10.0);
                    API.sendMessage(player, "&6在 &e" + player.getName() + " &6戶頭內增加了 &c10 &6元");
                } else {
                    Double b = API.getBalanceByName(type);
                    API.sendMessage(player, "&e" + player.getName() + " &6目前的餘額 : &c" + b + " &6元");
                }
            } else {
                double b = API.getBalanceByUuid(uuid);
                API.sendMessage(player, "&e" + player.getName() + " &6目前的餘額 : &c" + b + " &6元");
            }
        } else if (sender instanceof ConsoleCommandSender) {

        }
        return true;
    }

    public RootCommand() {
        Bukkit.getPluginCommand("money").setExecutor(this);
    }
}
