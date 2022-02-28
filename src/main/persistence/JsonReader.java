package persistence;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo

import model.Account;
import model.Wallet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Currency;
import java.util.stream.Stream;

// Represents a reader that reads Account from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Account read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAccount(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parse Account from JSON object and returns it
    private Account parseAccount(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Account account = new Account(name);
        addWallets(account, jsonObject);
        return account;
    }

    // MODIFIES: account
    // EFFECTS: parses wallets from JSON object and adds them to account
    private void addWallets(Account account, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("wallets");
        for (Object json : jsonArray) {
            JSONObject nextWallet = (JSONObject) json;
            addWallet(account, nextWallet);
        }
    }

    // MODIFIES: account
    // EFFECTS: adds a wallet to account from JSON object
    private void addWallet(Account account, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int id = jsonObject.getInt("id");
        double dollarBalance = jsonObject.getDouble("dollarBalance");
        Wallet wallet = new Wallet(name, id);
        wallet.deposit(dollarBalance);
        addBalances(wallet, jsonObject);
        account.addWallet(wallet);
    }

    // MODIFIES: wallet
    // EFFECTS: parses balances from JSON object and adds them to wallet
    private void addBalances(Wallet wallet, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("balances");
        for (Object json : jsonArray) {
            JSONObject nextBalance = (JSONObject) json;
            addBalance(wallet, nextBalance);
        }
    }

    // MODIFIES: wallet
    // EFFECTS: parses balance from JSON object and add it to wallet
    private void addBalance(Wallet wallet, JSONObject jsonObject) {
        String currencyName = jsonObject.getString("name");
        double currencyPrice = jsonObject.getDouble("price");


    }

}
