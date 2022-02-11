package model;

// Represents a currency
public interface Currency {

    // EFFECTS: returns price of currency
    double getPrice();

    // EFFECTS: returns name of currency
    String getName();

    // REQUIRES: amount > 0 & wallet must exist
    // MODIFIES: wallet
    // EFFECTS: adds amount currency to wallet
    void buy(Wallet wallet, double amount);

    // REQUIRES: amount > 0 & wallet must exist
    // MODIFIES: wallet
    // EFFECTS: adds amount currency to wallet
    void sell(Wallet wallet, double amount);

}
