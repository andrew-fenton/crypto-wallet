package ui;

import exceptions.BalanceNotFound;
import exceptions.BalancesIsEmpty;
import exceptions.CurrencyNotFound;
import model.*;

import java.util.ArrayList;
import java.util.Scanner;

// Wallet application -- referenced TellerApp example
public class WalletApp {

    private Account account;
    private Wallet wallet;
    private ArrayList<Wallet> wallets;
    private Scanner input;

    // Currencies
    private Bitcoin btc;
    private Ethereum eth;
    private Polkadot dot;

    // EFFECTS: runs the wallet application
    public WalletApp() {
        runWallet();
    }

    private void runWallet() {
        boolean active = true;
        String command;

        init();

        while (active) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equalsIgnoreCase("quit")) {
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

        // Initialize Currencies
        btc = new Bitcoin(1000);
        eth = new Ethereum(105.1);
        dot = new Polkadot(10.5);

        initRegister();
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
        System.out.println("\t Quit");
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
            double cost = (selectBalance(currencyInput).getCurrency().getPrice() * amount);
            boolean sufficientFunds = (wallet.getDollarBalance() >= cost);

            if (!sufficientFunds) {
                System.out.println("Insufficient funds.");
            } else if (amount < 0) {
                System.out.println("Amount must be greater than or equal to 0.");
            } else {
                wallet.buy(currency, amount);
                System.out.println("Purchase completed.");
                showBalances();
            }
        } catch (CurrencyNotFound e) {
            System.err.println("Currency, " + currencyInput + ", was not found.");
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            System.err.println("Balance list is empty or balance does not exist in wallet.");
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
            boolean sufficientFunds = selectBalance(currencyInput).getBalance() >= amount;

            if (!sufficientFunds) {
                System.out.println("Insufficient funds.");
            } else if (amount < 0) {
                System.out.println("Amount must be greater than or equal to 0.");
            } else {
                wallet.sell(currency, amount);
                System.out.println("Sale completed.");
                showBalances();
            }
        } catch (CurrencyNotFound e) {
            System.err.println("Currency, " + currencyInput + ", was not found.");
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            System.err.println("Balance list is empty or balance does not exist in wallet.");
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

    // EFFECTS: matches string input to existing currency and returns it
    private Balance selectBalance(String input) throws BalancesIsEmpty {
        for (Balance b : wallet.getBalances()) {
            if (b.getCurrency().getName().equalsIgnoreCase(input)) {
                return b;
            }
        }
        throw new BalancesIsEmpty();
    }

    private Currency matchCurrencyToInput(String input) throws CurrencyNotFound {
        for (Balance b : wallet.getBalances()) {
            if (b.getCurrency().getName().equalsIgnoreCase(input)) {
                return b.getCurrency();
            }
        }
        throw new CurrencyNotFound();
    }
}
