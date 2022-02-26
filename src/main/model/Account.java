package model;

import java.util.ArrayList;

public class Account {
    private String name;
    private ArrayList<Wallet> wallets;

    // EFFECTS: creates an empty list of wallets
    public Account(String name) {
        this.name = name;
        wallets = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a given wallet to wallets
    public boolean addWallet(Wallet wallet) {
        wallets.add(wallet);
        return true;
    }

    // EFFECTS: returns list of wallets
    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    // EFFECTS: returns name on account
    public String getName() {
        return this.name;
    }



}
