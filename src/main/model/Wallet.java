package model;

import exceptions.BalanceNotFound;
import exceptions.BalancesIsEmpty;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

// Represents a cryptocurrency wallet
public class Wallet implements Writeable {
    private String name;
    private int id;
    private double dollarBalance;
    private ArrayList<Balance> balances;

    // EFFECTS: creates a new crypto wallet with name, ID, and balance of 0.
    public Wallet(String name, int id) {
        this.name = name;
        this.id = id;
        this.dollarBalance = 0;
        balances = new ArrayList<>();
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: adds amount of money to wallet total balance
    public void deposit(double amount) {
        this.dollarBalance += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: if dollarBalance >= (currency.getPrice() * amount)
    //              - add amount to balance of given currency
    //              - subtract (currency.getPrice() * amount) from dollarBalance
    //              - return true
    //          - else return false
    public boolean buy(Currency currency, double amount) throws BalancesIsEmpty, BalanceNotFound {
        try {
            Balance balance = selectBalance(currency);

            if (amount >= 0) {
                if (dollarBalance >= (currency.getPrice() * amount)) {
                    balance.incrementBalance(amount);
                    dollarBalance -= (currency.getPrice() * amount);
                    return true;
                }
            }

            return false;
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            throw e;
        }
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: if balance of given currency >= amount
    //             - subtract amount from balance of given currency
    //             - add amount * currency.getPrice() to dollarBalance
    //             - return true
    //          - else return false
    public boolean sell(Currency currency, double amount) throws BalancesIsEmpty, BalanceNotFound {
        try {
            Balance balance = selectBalance(currency);

            if (amount >= 0) {
                if (balance.getBalance() >= amount) {
                    balance.reduceBalance(amount);
                    dollarBalance += (currency.getPrice() * amount);
                    return true;
                }
            }
            return false;
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            throw e;
        }
    }

    // REQUIRES: balances must not be empty
    public Balance selectBalance(Currency currency) throws BalancesIsEmpty, BalanceNotFound {
        if (!balances.isEmpty()) {
            for (Balance b : balances) {
                if (b.getCurrency().equals(currency)) {
                    return b;
                }
            }
            throw new BalanceNotFound("Balance not found.");
        }
        throw new BalancesIsEmpty("Balances is empty.");
    }

    // EFFECTS: returns wallet name
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns wallet ID
    public int getID() {
        return this.id;
    }

    // EFFECTS: returns wallet total balance
    public double getDollarBalance() {
        return this.dollarBalance;
    }

    // MODIFIES: this
    // EFFECTS: adds a balance to the list of balances
    public boolean addBalance(Balance balance) {
        if (!balances.contains(balance)) {
            this.balances.add(balance);
            return true;
        }
        return false;
    }

    // EFFECTS: returns balances
    public ArrayList<Balance> getBalances() {
        return balances;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: adds amount to given balance
    public boolean addToBalance(Currency currency, double amount) throws BalanceNotFound, BalancesIsEmpty {
        try {
            if (amount >= 0) {
                selectBalance(currency).incrementBalance(amount);
                return true;
            }
            return false;
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            throw e;
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("id", id);
        json.put("dollarBalance", dollarBalance);
        json.put("balances", balancesToJson());

        return json;
    }

    // EFFECTS: returns balances in this as a JSON array
    private JSONArray balancesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Balance b : balances) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }

}
