package me.miunapa.money.object;

import java.sql.Timestamp;

public class Record {
    int number;
    Timestamp timestamp;
    double vary;
    double balance;
    String remark;

    public Record(int number, Timestamp timestamp, double vary, double balance, String remark) {
        this.number = number;
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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
