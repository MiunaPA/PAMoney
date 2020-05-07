package me.miunapa.money.command.Sub;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.miunapa.money.command.SubCommand;
import me.miunapa.money.database.API;
import me.miunapa.money.object.Record;
import net.md_5.bungee.api.ChatColor;

public class Lookup extends SubCommand {
    public Lookup() {
        super("lookup", "pamoney.lookup",
                ChatColor.RED + "/money lookup [起始值] [ID]" + ChatColor.GRAY + "- 查詢交易記錄");
    }

    public boolean onCommand(CommandSender sender, Command commang, String label, String[] args) {
        String lookupName = "";
        Integer start = 0;
        if (args.length == 1) {
            if (sender instanceof Player) {
                lookupName = ((Player) sender).getName();
            } else {
                API.sendMessage(sender, "&c非玩家查詢時一定要輸入ID");
                return false;
            }
        } else if (args.length == 2) {
            if (sender instanceof Player) {
                lookupName = ((Player) sender).getName();
            } else {
                API.sendMessage(sender, "&c非玩家查詢時一定要輸入ID");
                return false;
            }
            try {
                start = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                API.sendMessage(sender, "&c起始值必須輸入為數字");
                return false;
            }
        } else if (args.length == 3) {
            lookupName = args[2];
            if (!API.hasBalanceByName(lookupName)) {
                API.sendMessage(sender, "&d此帳號不存在 無法查詢交易記錄");
                return false;
            }
            try {
                start = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                API.sendMessage(sender, "&c起始值必須輸入為數字");
                return false;
            }
        }
        searchRecord(sender, API.getUuidByName(lookupName), start);
        return false;
    }

    void searchRecord(CommandSender sender, String uuid, int start) {
        String name = API.getNameByUuid(uuid);
        List<Record> recordList = API.getRecordList(uuid, start, 15);
        if (recordList.size() == 0) {
            API.sendMessage(sender, "&c查詢不到記錄(可能是起始值太大)", true);
            return;
        } else {
            API.sendMessage(sender,
                    "&a查詢 &b" + name + " &a的交易紀錄如下&7(以第&c" + (start + 1) + "&7筆記錄 向前查詢15筆)", true);
        }
        for (Record record : recordList) {
            String varyText = "";
            if (record.getVary() > 0) {
                varyText = "&a" + record.getVary();
            } else if (record.getVary() < 0) {
                varyText = "&c" + record.getVary();
            } else {
                varyText = "&f" + record.getVary();
            }
            String text = "&3" + timestampToString(record.getTimestamp()) + " " + varyText
                    + " &7 -->  &e" + record.getBalance() + " &7(" + record.getRemark() + ")";
            API.sendMessage(sender, text, true);
        }
    }

    String timestampToString(Timestamp timestamp) {
        String result = "";
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        try {
            // 方法一
            result = sdf.format(timestamp);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
