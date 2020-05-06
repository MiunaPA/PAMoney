package me.miunapa.money.object;

import java.sql.Timestamp;

public class Record {
    Timestamp timestamp;
    double vary;
    double balance;
    String remark;

    public Record(Timestamp timestamp, double vary, double balance) {
        this.timestamp = timestamp;
        this.vary = vary;
        this.balance = balance;
        this.remark = "";
    }

    public Record(Timestamp timestamp, double vary, double balance, String remark) {
        this.timestamp = timestamp;
        this.vary = vary;
        this.balance = balance;
        this.remark = remark;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getVary() {
        return vary;
    }

    public void setVary(double vary) {
        this.vary = vary;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
