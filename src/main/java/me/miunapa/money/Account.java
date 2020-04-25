package me.miunapa.money;

public class Account {
    String uuid;
    String name;
    double amount;

    public Account(String uuid, String name, double amount) {
        this.uuid=uuid;
        this.name=name;
        this.amount = amount;
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
}
