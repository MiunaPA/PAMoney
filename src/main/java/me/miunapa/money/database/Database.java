package me.miunapa.money.database;

import java.util.List;
import me.miunapa.money.object.Account;

public interface Database {
    void initData();

    boolean getSetupStatus();

    // 金錢
    Double getBalanceByName(String name);

    Double getBalanceByUuid(String uuid);

    void setBalanceByName(String name, double balance);

    void setBalanceByUuid(String uuid, double balance);

    boolean hasBalanceByName(String name);

    boolean hasBalanceByUuid(String uuid);

    List<Account> getTop(int start, int count);

    // 交易紀錄資料庫
    void addRecord(String uuid, double vary, double balance);

    // 工具
    String getUuidByName(String name);
}
