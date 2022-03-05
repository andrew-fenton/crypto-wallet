package ui;

import exceptions.BalanceNotFound;
import exceptions.BalancesIsEmpty;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// Wallet application -- referenced TellerApp example
// https://github.students.cs.ubc.ca/CPSC210/TellerApp
public class WalletApp {
    private static final String PATH = "./data/account.json";
    private Account account;
    private Wallet wallet;
    private ArrayList<Wallet> wallets;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Currencies
    private Currency btc;
    private Currency eth;
    private Currency dot;

    // EFFECTS: runs the wallet application
    public WalletApp() throws FileNotFoundException {
        runWallet();
    }

    private void runWallet() {
        boolean active = true;
        String command;

        init();

        while (active) {
            displayMenu();
            command = input.next();

            if (command.equalsIgnoreCase("quit")) {
                saveToFile();
                active = false;
            } else {
                processCommand(command);
            }
        }
    }

    // EFFECTS: initializes wallet and currency
    private void init() {
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(PATH);
        jsonReader = new JsonReader(PATH);

        // Initialize Currencies
        btc = new Currency("Bitcoin", 1000);
        eth = new Currency("Ethereum", 105.1);
        dot = new Currency("Polkadot", 10.5);

        System.out.println("\t Would you like to load your previous account?");
        processLoadOption(input.next());
    }

    // REQUIRES: must add all instantiated balances
    // EFFECTS: adds instantiated balances to wallet balance list on registration
    private void initBalances() {
        wallet.addBalance(new Balance(btc));
        wallet.addBalance(new Balance(eth));
        wallet.addBalance(new Balance(dot));
    }

    // MODIFIES: this
    // EFFECTS: creates a new account and wallet
    private void initRegister() {
        System.out.println("\n Enter account name: ");
        String accountName = input.next();
        account = new Account(accountName);
        wallets = account.getWallets();

        System.out.println("\n Enter wallet name: ");
        String name = input.next();
        int id = wallets.size();

        wallet = new Wallet(name, id);
        initBalances();
        wallets.add(id, wallet);
    }

    // MODIFIES: this
    // EFFECTS: registers a new wallet
    private void register() {
        wallets = account.getWallets();

        System.out.println("\n Enter wallet name: ");
        String name = input.next();
        int id = wallets.size();

        wallet = new Wallet(name, id);
        initBalances();
        wallets.add(id, wallet);
    }

    // EFFECTS: displays menu
    private void displayMenu() {
        System.out.println("\n Current wallet: " + wallet.getName() + " | ID: " + wallet.getID());
        System.out.println("\t Buy");
        System.out.println("\t Sell");
        System.out.println("\t Deposit");
        System.out.println("\t Balances");
        System.out.println("\t Register - create new wallet");
        System.out.println("\t Wallets - show existing wallets");
        System.out.println("\t Select - change wallet");
        System.out.println("\t Save - save current account");
        System.out.println("\t Quit");
    }

    // EFFECTS: processes load menu option
    private void processLoadOption(String command) {
        if (command.equalsIgnoreCase("yes")) {
            loadFromFile();
        } else if (command.equalsIgnoreCase("no")) {
            initRegister();
        } else {
            System.out.println("Invalid: please type yes or no.");
        }
    }

    // EFFECTS: checks if user input matches any option on the menu
    private void processCommand(String command) {
        if (command.equalsIgnoreCase("buy")) {
            doBuy();
        } else if (command.equalsIgnoreCase("sell")) {
            doSell();
        } else if (command.equalsIgnoreCase("deposit")) {
            doDeposit();
        } else if (command.equalsIgnoreCase("balances")) {
            showBalances();
        } else if (command.equalsIgnoreCase("register")) {
            register();
        } else if (command.equalsIgnoreCase("wallets")) {
            showWallets();
        } else if (command.equalsIgnoreCase("select")) {
            selectWallet();
        } else if (command.equalsIgnoreCase("save")) {
            saveToFile();
        } else {
            System.out.println("Invalid input.");
        }
    }

    // REQUIRES: must input a number
    // MODIFIES: this
    // EFFECTS: conducts a deposit
    private void doDeposit() {
        showBalances();
        System.out.println("Enter amount to deposit:");
        double amount = input.nextDouble();

        if (amount >= 0) {
            wallet.deposit(amount);
            System.out.println("Deposit completed.");
            showBalances();
        } else {
            System.out.println("Amount must be greater than or equal to 0.");
        }
    }

    // REQUIRES: must input string for type of currency and number for amount
    // MODIFIES: this
    // EFFECTS: conducts a purchase of cryptocurrency
    private void doBuy() {
        showBalances();
        System.out.println("Enter type of currency to buy: ");
        String currencyInput = input.next();
        System.out.println("Enter amount to buy: ");
        double amount = input.nextDouble();

        try {
            Currency currency = matchCurrencyToInput(currencyInput);

            if (currency != null) {
                boolean sufficientFunds = (wallet.getDollarBalance() >= (currency.getPrice() * amount));

                if (approveValues(sufficientFunds, amount)) {
                    wallet.buy(currency, amount);
                    System.out.println("Purchase completed.");
                    showBalances();
                }

            } else {
                System.err.println("Currency, " + currencyInput + ", was not found.");
            }
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    // REQUIRES: must input string for type of currency and number for amount
    // MODIFIES: this
    // EFFECTS: conducts a sale of cryptocurrency
    private void doSell() {
        showBalances();
        System.out.println("Enter type of currency to sell: ");
        String currencyInput = input.next();
        System.out.println("Enter amount to sell: ");
        double amount = input.nextDouble();

        try {
            Currency currency = matchCurrencyToInput(currencyInput);

            if (currency != null) {
                boolean sufficientFunds = selectBalance(currencyInput).getBalance() >= amount;

                if (approveValues(sufficientFunds, amount)) {
                    wallet.sell(currency, amount);
                    System.out.println("Sale completed.");
                    showBalances();
                }

            } else {
                System.err.println("Currency, " + currencyInput + ", was not found.");
            }
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    // EFFECTS: shows all balances in wallet
    private void showBalances() {
        System.out.println("\n Balances:");
        System.out.println("\t Total Balance: " + wallet.getDollarBalance());

        for (Balance b : wallet.getBalances()) {
            System.out.println("\t " + b.getCurrency().getName() + " Balance: " + b.getBalance());
        }
    }

    // EFFECTS: shows all wallets
    private void showWallets() {
        System.out.println("\n Wallets: ");
        int count = 1;

        for (Wallet w : wallets) {
            System.out.println(count + ") Name: " + w.getName() + "\t ID: " + w.getID());
            count++;
        }
    }

    // MODIFIES: this
    // EFFECTS: selects wallet from wallets
    private void selectWallet() {
        showWallets();
        System.out.println("Select wallet by typing wallet ID: ");
        int id = input.nextInt();

        if (id < wallets.size() && id >= 0) {
            wallet = wallets.get(id);
            System.out.println("Wallet Selected: " + wallet.getName() + " - " + wallet.getID());
        } else {
            System.out.println("Enter a valid wallet ID.");
        }
    }

    // EFFECTS: matches string input to existing currency and returns its respective balance in wallet
    //          throws
    private Balance selectBalance(String input) throws BalanceNotFound {
        for (Balance b : wallet.getBalances()) {
            if (b.getCurrency().getName().equalsIgnoreCase(input)) {
                return b;
            }
        }
        throw new BalanceNotFound("Input could not be matched to: " + input);
    }

    // EFFECTS: matches a string input to a currency and returns it
    private Currency matchCurrencyToInput(String input) {
        for (Balance b : wallet.getBalances()) {
            if (b.getCurrency().getName().equalsIgnoreCase(input)) {
                return b.getCurrency();
            }
        }
        return null;
    }

    // EFFECTS: checks if there are sufficient funds to perform the transaction and if
    //          amount >= 0
    private boolean approveValues(boolean sufficientFunds, double amount) {
        if (!sufficientFunds) {
            System.out.println("Insufficient funds.");
            return false;
        } else if (amount < 0) {
            System.out.println("Amount must be greater than or equal to 0.");
            return false;
        } else {
            return true;
        }
    }

    // EFFECTS: saves account to JSON file
    private void saveToFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();
            System.out.println("Saved " + account.getName() + " to " + PATH);
        } catch (FileNotFoundException e) {
            System.err.println("File could not be found at " + PATH);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads account from JSON file
    private void loadFromFile() {
        try {
            account = jsonReader.read();
            wallets = account.getWallets();
            wallet = account.getWallets().get(0);
            System.out.println("Loaded " + account.getName() + " from " + PATH);
        } catch (IOException e) {
            System.err.println("Unable to read from file: " + PATH);
        }
    }
}
