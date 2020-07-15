package me.miunapa.money.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class SubCommand {
    private final String name;
    private final String permission;
    private final String helpMessage;
    public Plugin plugin = Bukkit.getPluginManager().getPlugin("PA_Money");

    public SubCommand(String name, String permission, String helpMessage) {
        this.name = name;
        this.permission = permission;
        this.helpMessage = helpMessage;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String commandLabel,
            String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getHelpMessage() {
        return helpMessage;
    }
}
