package model;

// Represents a type of currency
public class Ethereum implements Currency {

    private String name;
    private double price;

    // EFFECTS: constructs ethereum currency with given price
    public Ethereum(double price) {
        this.name = "Ethereum";
        this.price = price;
    }

    @Override
    // REQUIRES: amount >= 0 && insufficient balance checks are already done
    // MODIFIES: wallet
    // EFFECTS: adds amount to wallet currency balance
    public void buy(Wallet wallet, double amount) {
        wallet.addEthBalance(amount);
        wallet.setTotalBalance(wallet.getTotalBalance() - this.price * amount);
    }

    @Override
    // REQUIRES: amount >= 0 && insufficient balance checks are already done
    // MODIFIES: wallet
    // EFFECTS: subtracts amount from wallet currency balance
    public void sell(Wallet wallet, double amount) {
        wallet.setEthBalance(wallet.getEthBalance() - amount);
        wallet.deposit(this.price * amount);
    }

    @Override
    // EFFECTS: returns price of currency
    public double getPrice() {
        return this.price;
    }

    @Override
    // EFFECTS: returns name of currency
    public String getName() {
        return this.name;
    }
}
