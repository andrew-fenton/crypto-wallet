package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;
import java.util.Observable;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class Account implements Writeable {
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
        EventLog.getInstance().logEvent(new Event("Added a new wallet to account: " + wallet));
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

    @Override
    // EFFECTS: returns Account as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("wallets", walletsToJson());

        return json;
    }

    // EFFECTS: returns wallets in this as a JSON array
    private JSONArray walletsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Wallet w : wallets) {
            jsonArray.put(w.toJson());
        }

        return jsonArray;
    }
}
