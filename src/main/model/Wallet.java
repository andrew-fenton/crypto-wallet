package model;

// Represents a cryptocurrency wallet
public class Wallet {

    private String name;
    private int id;
    private double totalBalance;
    private double ethBalance;

    // EFFECTS: creates a new crypto wallet with name, ID, and balance of 0.
    public Wallet(String name, int id) {
        this.name = name;
        this.id = id;
        this.totalBalance = 0;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: adds amount of money to wallet total balance
    public void deposit(double amount) {
        this.totalBalance += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: if totalBalance >= (currency.getPrice() * amount)
    //              - add amount to currencyBalance
    //              - subtract (currency.getPrice() * amount) from totalBalance
    //              - return true
    //          - else return false
    public boolean buy(Currency currency, double amount) {
        if (totalBalance >= (currency.getPrice() * amount)) {
            if (currency.getName().equals("Ethereum")) {
                currency.buy(this, amount);
                return true;
            }
        }

        return false;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: if balance of currency >= amount
    //             - subtract amount from currencyBalance
    //             - add amount * currency.getPrice() to totalBalance
    //             - return true
    //          - else return false
    public boolean sell(Currency currency, double amount) {
        if (ethBalance >= amount && currency.getName().equals("Ethereum")) {
            currency.sell(this, amount);
            return true;
        }

        return false;
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
    public double getTotalBalance() {
        return this.totalBalance;
    }

    // EFFECTS: returns wallet ethereum balance
    public double getEthBalance() {
        return this.ethBalance;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: sets totalBalance to amount
    public void setTotalBalance(double amount) {
        this.totalBalance = amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: adds amount to wallet ethereum balance
    public void addEthBalance(double amount) {
        this.ethBalance += amount;
    }

    // REQUIRES: amount >= 0
    // MODIFIES: this
    // EFFECTS: sets wallet ethereum balance to amount
    public void setEthBalance(double amount) {
        this.ethBalance = amount;
    }

}
