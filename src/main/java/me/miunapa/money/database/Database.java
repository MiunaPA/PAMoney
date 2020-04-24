package me.miunapa.money.database;

public interface Database {
    void initData();

    boolean getSetupStatus();

    Double getBalanceByName(String name);

    Double getBalanceByUuid(String uuid);

    void setBalanceByName(String name, double balance);

    void setBalanceByUuid(String uuid, double balance);

    boolean hasBalanceByName(String name);

    boolean hasBalanceByUuid(String uuid);
}
