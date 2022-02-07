package ui;

import model.Currency;
import model.Ethereum;
import model.Wallet;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

// Wallet application -- referenced TellerApp example
public class WalletApp {

    private Wallet wallet;
    private Ethereum eth;
    private Scanner input;
    private ArrayList<Wallet> wallets;

    // runs the wallet application
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

        System.out.println("\n Enter name: ");
        String name = input.next();
        System.out.println("\n Enter ID: ");
        int id = input.nextInt();

        wallet = new Wallet(name, id);
        eth = new Ethereum(105.1);
    }

    // EFFECTS: displays menu
    public void displayMenu() {
        System.out.println("\n Select by typing name:");
        System.out.println("\t Buy");
        System.out.println("\t Sell");
        System.out.println("\t Deposit");
        System.out.println("\t View Balances");
        System.out.println("\t Quit");
    }

    // EFFECTS: checks if user input matches any option on the menu
    public void processCommand(String command) {
        if (command.equalsIgnoreCase("buy")) {
            doBuy();
        } else if (command.equalsIgnoreCase("sell")) {
            doSell();
        } else if (command.equalsIgnoreCase("deposit")) {
            doDeposit();
        } else if (command.equalsIgnoreCase("view balances")) {
            showBalances();
        } else {
            System.out.println("Invalid input.");
        }
    }

    // MODIFIES: this
    // EFFECTS: conducts a deposit
    public void doDeposit() {
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
    public void doBuy() {
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
    public void doSell() {
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
    public void showBalances() {
        System.out.println("\n Balances:");
        System.out.println("\t Total Balance: " + wallet.getTotalBalance());
        System.out.println("\t Ethereum Balance: " + wallet.getEthBalance());
    }
}
