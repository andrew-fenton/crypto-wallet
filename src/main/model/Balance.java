package model;

import org.json.JSONObject;
import persistence.Writeable;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a balance of specific currency
public class Balance implements Writeable {
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

    // EFFECTS: returns this as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("currency", currency.toJson());
        json.put("balance", balance);
        return json;
    }
}
