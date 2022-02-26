package model;

public class Balance {
    private Currency currency;
    private double balance;

    // EFFECTS: constructs a balance of type currency with zero balance
    public Balance(Currency currency) {
        this.currency = currency;
        this.balance = 0;
    }

    // EFFECTS: returns currency
    public Currency getCurrency() {
        return currency;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: adds amount to balance
    public void incrementBalance(double amount) {
        this.balance += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: reduces balance by given amount
    public void reduceBalance(double amount) {
        this.balance -= amount;
    }

    // EFFECTS: returns balance
    public double getBalance() {
        return balance;
    }
}
