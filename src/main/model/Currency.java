package model;

// Represents a currency
public interface Currency {

    // EFFECTS: returns price of currency
    double getPrice();

    // EFFECTS: returns name of currency
    String getName();

    void buy(Wallet wallet, double amount);

    void sell(Wallet wallet, double amount);

}
