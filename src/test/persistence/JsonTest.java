package persistence;

import model.Balance;
import model.Wallet;

import static org.junit.jupiter.api.Assertions.*;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected void checkWallet(Wallet wallet, String name, int id, double dollarBalance) {
        assertEquals(name, wallet.getName());
        assertEquals(id, wallet.getID());
        assertEquals(dollarBalance, wallet.getDollarBalance());
    }

    protected void checkBalance(Balance balance, String currencyName, double currencyPrice, double amount) {
        assertEquals(currencyName, balance.getCurrency().getName());
        assertEquals(currencyPrice, balance.getCurrency().getPrice());
        assertEquals(amount, balance.getBalance());
    }

}
