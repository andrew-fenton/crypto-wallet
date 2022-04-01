package ui;

import exceptions.BalanceNotFound;
import exceptions.BalancesIsEmpty;
import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

// Represents GUI of an account
public class AccountUI extends JFrame {
    private static final String PATH = "./data/account.json";
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    private Account account;
    private List<Wallet> wallets;
    private DefaultListModel<Wallet> model;
    private Wallet selectedWallet;

    private JSplitPane mainSplitPane;
    private JLabel label;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // Currencies
    private Currency btc;
    private Currency eth;
    private Currency dot;

    // EFFECTS: constructs an account GUI
    public AccountUI() {
        super("Cryptocurrency Wallet");

        // Initialize Currencies
        btc = new Currency("Bitcoin", 1000);
        eth = new Currency("Ethereum", 105.1);
        dot = new Currency("Polkadot", 10.5);

        mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        initializeGraphics();
        initialize();
        addButtonPanel();
        addWalletDisplay();

        mainSplitPane.setResizeWeight(0.93);
        add(mainSplitPane);
    }

    // MODIFIES: this
    // EFFECTS: initializes variables for data persistence and loads account from file if yes,
    //          makes new account if no
    private void initialize() {
        label = new JLabel();
        jsonWriter = new JsonWriter(PATH);
        jsonReader = new JsonReader(PATH);

        int answer = JOptionPane.showConfirmDialog(null,
                "Would you like to load your account from file?",
                "Load from File",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            loadFromFile();
        } else {
            initializeFields();
        }

        askToSave();
    }

    // MODIFIES: this
    // EFFECTS: loads account from JSON file
    private void loadFromFile() {
        try {
            account = jsonReader.read();
            wallets = account.getWallets();
            selectedWallet = account.getWallets().get(0);
            System.out.println("Loaded " + account.getName() + " from " + PATH);
        } catch (IOException e) {
            System.err.println("Unable to read from file: " + PATH);
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

    // EFFECTS: prints log of actions performed while running
    private void printLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.getDate() + ": " + e.getDescription());
        }
    }

    // EFFECTS: asks to save account to file on close
    private void askToSave() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                int answer = JOptionPane.showConfirmDialog(
                        null,
                        "Would you like to save your account to file?",
                        "Save to File",
                        JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                    saveToFile();
                }

                printLog();
                dispose();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS:  draws the JFrame window where this DrawingEditor will operate, and populates the tools to be used
    //           to manipulate this drawing
    private void initializeGraphics() {
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: initializes fields with one wallet in account
    private void initializeFields() {
        String name = JOptionPane.showInputDialog(null,
                "Enter Account Name: ",
                "Register Account",
                JOptionPane.QUESTION_MESSAGE);

        if (name != null) {
            account = new Account("name");
            wallets = account.getWallets();

            String walletName = JOptionPane.showInputDialog(null,
                    "Enter Wallet Name: ",
                    "Register Wallet",
                    JOptionPane.QUESTION_MESSAGE);

            Wallet wallet = new Wallet(walletName, wallets.size());
            initBalances(wallet);
            account.addWallet(wallet);
            selectedWallet = wallet;
        }
    }

    // EFFECTS: adds button panel to main JFrame
    private void addButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.setSize(new Dimension(WIDTH, 100));
        panel.add(new JButton(new AddWalletAction()));
        panel.add(new JButton(new DepositAction()));
        panel.add(new JButton(new BuyAction()));
        panel.add(new JButton(new SellAction()));
        panel.add(new JLabel(new ImageIcon("./images/stock_arrow.png")));
        mainSplitPane.setBottomComponent(panel);
    }

    // EFFECTS: adds display of all wallets in wallets
    private void addWalletDisplay() {
        JList<Wallet> walletJList = new JList<>();
        model = new DefaultListModel<>();

        JSplitPane splitPane = new JSplitPane();
        JPanel panel = new JPanel();

        walletJList.setModel(model);

        for (Wallet w : wallets) {
            model.addElement(w);
        }

        walletJList.getSelectionModel().addListSelectionListener(e -> {
            selectedWallet = walletJList.getSelectedValue();
            updateBalances();
        });

        splitPane.setLeftComponent(new JScrollPane(walletJList));
        panel.add(label);
        splitPane.setRightComponent(panel);
        splitPane.setResizeWeight(0.3);
        mainSplitPane.setTopComponent(splitPane);
    }

    // EFFECTS: updates the right pane label of mainSplitPane
    private void updateBalances() {
        label.setText(printBalances(selectedWallet));
    }

    // EFFECTS: prints balances of given wallet
    private String printBalances(Wallet wallet) {
        String balances = "USD: " + wallet.getDollarBalance();

        for (Balance b : wallet.getBalances()) {
            balances += "\n " + b.getCurrency() + ": " + b.getBalance();
        }

        return balances;
    }

    // MODIFIES: this
    // EFFECTS: adds newly added wallet to wallet display and updates balances in GUI
    private void refreshWallets() {
        for (Wallet w : wallets) {
            if (!model.contains(w)) {
                model.addElement(w);
            }
        }
        updateBalances();
    }

    // REQUIRES: must add all instantiated balances
    // EFFECTS: adds instantiated balances to given wallet balance list
    private void initBalances(Wallet wallet) {
        wallet.addBalance(new Balance(btc));
        wallet.addBalance(new Balance(eth));
        wallet.addBalance(new Balance(dot));
    }

    // MODIFIES: this
    // EFFECTS: adds a new wallet to account
    private void addWallet(Wallet wallet) {
        account.addWallet(wallet);
    }

    // EFFECTS: returns this.wallets size
    private int getWalletsSize() {
        return wallets.size();
    }

    // EFFECTS: returns names of currencies
    private String[] getCurrencyNames() {
        List<Balance> balances = selectedWallet.getBalances();
        String[] names = new String[balances.size()];
        int count = 0;

        for (Balance b : balances) {
            names[count] = b.getCurrency().getName();
            count++;
        }

        return names;
    }

    // EFFECTS: converts a string into a currency
    private Currency stringToCurrency(Object obj) {
        for (Balance b : selectedWallet.getBalances()) {
            if (b.getCurrency().getName().equalsIgnoreCase(obj.toString())) {
                return b.getCurrency();
            }
        }
        return null;
    }

    // EFFECTS: checks if there are sufficient funds to perform the transaction and if
    //          amount >= 0
    private boolean approveValues(boolean sufficientFunds, double amount) {
        if (!sufficientFunds) {
            JOptionPane.showMessageDialog(
                    null,
                    "Insufficient funds.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (amount < 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "Amount must be greater than or equal to 0.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    // EFFECTS: returns selectedWallet's balance for given currency
    private Balance balanceFromCurrency(Currency currency) {
        for (Balance b : selectedWallet.getBalances()) {
            if (b.getCurrency().equals(currency)) {
                return b;
            }
        }
        return null;
    }

    // EFFECTS: runs account GUI
    public static void main(String[] args) {
        new AccountUI();
    }

    /**
     * Represents the action when a user wants to add a new wallet to their account.
    */
    private class AddWalletAction extends AbstractAction {

        // EFFECTS: constructs an add wallet action
        public AddWalletAction() {
            super("Add Wallet");
        }

        // MODIFIES: this
        // EFFECTS: creates a new wallet and adds it to account
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog(null,
                    "Enter Wallet Name:",
                    "Wallet Name",
                    JOptionPane.QUESTION_MESSAGE);

            if (name != null) {
                Wallet w = new Wallet(name, getWalletsSize());
                initBalances(w);
                addWallet(w);
                refreshWallets();
            }
        }
    }

    /**
     * Represents the action when a user wants to add a new wallet to their account.
     */
    private class DepositAction extends AbstractAction {

        // EFFECTS: constructs a deposit action
        public DepositAction() {
            super("Deposit");
        }

        // REQUIRES: amount must be a number and >= 0
        // MODIFIES: this
        // EFFECTS: adds amount to selectedWallet dollarBalance
        @Override
        public void actionPerformed(ActionEvent e) {
            double amount = Double.parseDouble(JOptionPane.showInputDialog(null,
                    "Enter Deposit Amount:",
                    "Deposit",
                    JOptionPane.QUESTION_MESSAGE));

            if (amount >= 0) {
                selectedWallet.deposit(amount);
                refreshWallets();
            } else {
                JOptionPane.showConfirmDialog(
                        null,
                        "Amount must be greater than or equal to 0.");
            }
        }
    }

    /**
     * Represents the action when a user buys cryptocurrency.
     */
    private class BuyAction extends AbstractAction {

        // EFFECTS: constructs a buy action
        public BuyAction() {
            super("Buy");
        }

        // REQUIRES: amount must be a number and >= 0
        // MODIFIES: this
        // EFFECTS: conducts a purchase of cryptocurrency
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] currencies = getCurrencyNames();

            Object selectedCurrency = JOptionPane.showInputDialog(null,
                    "Select Currency to Buy",
                    "Currency Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    currencies,
                    currencies[0]);

            double amount = Double.parseDouble(JOptionPane.showInputDialog(null,
                    "Enter Amount to Buy"));

            try {
                Currency currency = stringToCurrency(selectedCurrency);

                if (currency != null) {
                    boolean sufficientFunds = (selectedWallet.getDollarBalance() >= (currency.getPrice() * amount));

                    if (approveValues(sufficientFunds, amount)) {
                        selectedWallet.buy(currency, amount);
                        refreshWallets();
                    }

                }

            } catch (BalancesIsEmpty | BalanceNotFound x) {
                JOptionPane.showConfirmDialog(null, x.getMessage());
            }
        }
    }

    /**
     * Represents the action when a user sells cryptocurrency.
     */
    private class SellAction extends AbstractAction {

        // EFFECTS: constructs a sell action
        public SellAction() {
            super("Sell");
        }

        // REQUIRES: amount must be a number and >= 0
        // MODIFIES: this
        // EFFECTS: conducts a sale of cryptocurrency
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] currencies = getCurrencyNames();

            Object selectedCurrency = JOptionPane.showInputDialog(null,
                    "Select Currency to Buy",
                    "Currency Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    currencies,
                    currencies[0]);

            double amount = Double.parseDouble(JOptionPane.showInputDialog(null,
                    "Enter Amount to Sell"));

            try {
                Currency currency = stringToCurrency(selectedCurrency);

                if (currency != null) {
                    boolean sufficientFunds = balanceFromCurrency(currency).getBalance() >= amount;

                    if (approveValues(sufficientFunds, amount)) {
                        selectedWallet.sell(currency, amount);
                        System.out.println("Sale completed.");
                        refreshWallets();
                    }

                }

            } catch (BalancesIsEmpty | BalanceNotFound x) {
                JOptionPane.showConfirmDialog(null, x.getMessage());
            }

        }
    }

}


