package me.miunapa.money;

public class Account {
    String uuid;
    String name;
    double amount;
    int rank;

    public Account(String uuid, String name, double amount) {
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
        this.rank = 0;
    }

    public Account(String uuid, String name, double amount, int rank) {
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
        this.rank = rank;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
