package ui;

import model.Ethereum;
import model.Wallet;

import java.util.ArrayList;
import java.util.Scanner;

// Wallet application -- referenced TellerApp example
public class WalletApp {

    private Wallet wallet;
    private ArrayList<Wallet> wallets;
    private Ethereum eth;
    private Scanner input;

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
        wallets = new ArrayList<>();

        register();

        eth = new Ethereum(105.1);
    }

    // EFFECTS: creates a new wallet
    private void register() {
        System.out.println("\n Enter name: ");
        String name = input.next();
        int id = wallets.size();

        wallet = new Wallet(name, id);
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

    // MODIFIES: this
    // EFFECTS: conducts a purchase of cryptocurrency
    private void doBuy() {
        showBalances();
        System.out.println("Enter type of currency to buy: ");
        String currency = input.next();
        System.out.println("Enter amount to buy: ");
        double amount = input.nextDouble();

        boolean sufficientFunds = wallet.getTotalBalance() >= (amount * eth.getPrice());

        if (currency.equalsIgnoreCase("ethereum") && amount >= 0 && sufficientFunds) {
            wallet.buy(eth, amount);
            System.out.println("Purchase completed.");
            showBalances();
        } else if (amount < 0) {
            System.out.println("Amount must be greater than or equal to 0.");
        } else if (!sufficientFunds) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("Currency, " + currency + ", was not found.");
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts a sale of cryptocurrency
    private void doSell() {
        showBalances();
        System.out.println("Enter type of currency to sell: ");
        String currency = input.next();
        System.out.println("Enter amount to sell: ");
        double amount = input.nextDouble();

        boolean sufficientFunds = wallet.getEthBalance() >= amount;

        if (currency.equalsIgnoreCase("ethereum") && amount >= 0 && sufficientFunds) {
            wallet.sell(eth, amount);
            System.out.println("Sale completed.");
            showBalances();
        } else if (amount < 0) {
            System.out.println("Amount must be greater than or equal to 0.");
        } else if (!sufficientFunds) {
            System.out.println("Insufficient funds.");
        } else {
            System.out.println("Currency, " + currency + ", was not found.");
        }
    }

    // EFFECTS: shows all balances in wallet
    private void showBalances() {
        System.out.println("\n Balances:");
        System.out.println("\t Total Balance: " + wallet.getTotalBalance());
        System.out.println("\t Ethereum Balance: " + wallet.getEthBalance());
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
}
