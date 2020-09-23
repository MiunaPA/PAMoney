package me.miunapa.money.database;

import java.util.List;
import me.miunapa.money.object.Account;
import me.miunapa.money.object.Record;

public interface Database {
    // 資料庫初始化
    void initData();

    boolean getSetupStatus();

    // 工具
    String getUuidByName(String name);

    String getNameByUuid(String uuid);

    // 金錢
    Double getBalanceByName(String name);

    Double getBalanceByUuid(String uuid);

    void setBalanceByName(String name, double balance);

    void setBalanceByUuid(String uuid, double balance);

    boolean hasBalanceByName(String name);

    boolean hasBalanceByUuid(String uuid);

    List<Account> getTop(int start, int count);

    Double getSumBalance(double minBalance);

    // 交易紀錄資料庫
    void addRecord(String uuid, double vary, double balance, String remark);

    List<Record> getRecord(String uuid, int start, int count);

    // 資料庫連線
    void reconnect();

    void disconnect();
}
