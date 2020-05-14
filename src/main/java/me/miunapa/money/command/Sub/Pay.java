package me.miunapa.money.command.Sub;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

public class Pay extends SubCommand {
    HashMap<Player, Integer> payMap = new HashMap<Player, Integer>();
    // <付錢的人,隨機數字>

    public Pay() {
        super("pay", "pamoney.pay",
                ChatColor.RED + "/money pay <ID> <amount> " + ChatColor.GRAY + "- 給別人錢");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        if (!(sender instanceof Player)) {
            API.sendMessage(sender, "&e給錢的指令只有玩家才能使用");
            return false;
        }
        Player player = (Player) sender;
        if (args.length != 3 && args.length != 4) {
            API.sendMessage(player, this.getHelpMessage());
            return false;
        }
        String payName = args[1];
        if (player.getName().equals(payName)) {
            API.sendMessage(player, "&c不能給自己錢");
            return false;
        }
        if (!API.hasBalanceByName(payName)) {
            API.sendMessage(player, "&d此帳號不存在 無法給錢");
            return false;
        }
        double payerAmount, payAmount;
        if (args.length == 3) {
            try {
                payerAmount = API.getBalanceByUuid(player.getUniqueId().toString());
                payAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
            } catch (NumberFormatException e) {
                API.sendMessage(player, "&c金額必須輸入數字(可有小數點)");
                return false;
            }
            if (payerAmount < payAmount) {
                API.sendMessage(player, "&c你的錢不夠了");
                return false;
            }
            if (!((API.getBalanceByName(payName) + payAmount) < API.getConfig()
                    .getDouble("limit_money"))) {
                API.sendMessage(player, "&b" + payName + "&d 擁有的錢會超過上限 無法執行");
                return false;
            }
            payRequest(player, payName, payAmount);

        } else if (args.length == 4) {
            try {
                payerAmount = API.getBalanceByUuid(player.getUniqueId().toString());
                payAmount = API.formatAmountdouble(Double.parseDouble(args[2]));
            } catch (NumberFormatException e) {
                API.sendMessage(player, "&c金額必須輸入數字(可有小數點)[arg4]");
                return false;
            }
            Integer confirmCode;
            try {
                confirmCode = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                API.sendMessage(player, "&c驗證代碼轉換錯誤[arg4]");
                return false;
            }
            if (payerAmount < payAmount) {
                API.sendMessage(player, "&c你的錢不夠了[arg4]");
                return false;
            }
            if (!((API.getBalanceByName(payName) + payAmount) < API.getConfig()
                    .getDouble("limit_money"))) {
                API.sendMessage(player, "&b" + payName + "&d 擁有的錢會超過上限 無法執行[arg4]");
                return false;
            }
            payConfirm(player, payName, payAmount, confirmCode);
        }
        return false;
    }

    void payRequest(Player player, String payName, double payAmount) {
        Random random = new Random();
        Integer rd = random.nextInt(87638763) + 1;
        if (payMap.containsKey(player)) {
            API.sendMessage(player, "&7上一個轉帳請求已取消");
        }
        payMap.put(player, rd);
        TextComponent accept = new TextComponent(
                ChatColor.GRAY + "【" + ChatColor.GREEN + "確認" + ChatColor.GRAY + "】");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                "/money pay " + payName + " " + payAmount + " " + rd.toString()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("點我").color(ChatColor.GREEN).append(" 以確認轉帳")
                        .color(ChatColor.GREEN).append("\n轉帳給 ").color(ChatColor.YELLOW)
                        .append(payName).color(ChatColor.AQUA).append(" " + payAmount)
                        .color(ChatColor.YELLOW).append(" 元").create()));
        TextComponent deny = new TextComponent(
                ChatColor.GRAY + "【" + ChatColor.RED + "取消" + ChatColor.GRAY + "】");
        deny.setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/money pay " + payName + " 0 0"));
        deny.setHoverEvent(new HoverEvent(Action.SHOW_TEXT,
                new ComponentBuilder("取消交易").color(ChatColor.RED).create()));
        TextComponent ask =
                new TextComponent(API.getPrefix() + ChatColor.YELLOW + "你確定要給 " + ChatColor.AQUA
                        + payName + " " + ChatColor.RED + payAmount + ChatColor.YELLOW + " 元嗎?");
        player.spigot().sendMessage(new ComponentBuilder(ask).append(accept).append(deny).create());
    }

    void payConfirm(Player player, String payName, double payAmount, Integer confirmCode) {
        if (!payMap.containsKey(player)) {
            API.sendMessage(player, "&c轉帳請求不存在或已經完成");
            return;
        }
        if (confirmCode == 0) {
            API.sendMessage(player, "已取消此轉帳請求");
            payMap.remove(player);
            return;
        }
        if (!payMap.get(player).equals(confirmCode)) {
            API.sendMessage(player, "&c轉帳請求已經完成或被覆蓋 轉帳失敗");
            return;
        }
        API.withdraw(player.getName(), payAmount, "轉帳給:" + payName);
        API.deposit(payName, payAmount, "收到轉帳:" + player.getName());
        API.sendMessage(player, "&a已將 &c" + payAmount + " &a元 轉帳給 &b" + payName);
        if (Bukkit.getPlayer(payName) != null) {
            API.sendMessage(Bukkit.getPlayer(payName),
                    "&a你收到了 &b" + player.getName() + " &a的 &c" + payAmount + " &a元轉帳");
        }
        payMap.remove(player);
    }
}
