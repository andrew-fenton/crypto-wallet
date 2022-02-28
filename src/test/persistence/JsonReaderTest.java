package persistence;

import model.Account;
import model.Balance;
import model.Wallet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest extends JsonTest {
    private Account account;
    private JsonReader jsonReader;

    @Test
    public void testReaderFileNotFound() {
        jsonReader = new JsonReader("./data/doesNotExist.json");
        try {
            account = jsonReader.read();
            fail("This should not run.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReaderEmptyAccount() {
        jsonReader = new JsonReader("./data/testEmptyAccount.json");
        try {
            account = jsonReader.read();
            assertEquals("Test", account.getName());
            assertEquals(0, account.getWallets().size());
        } catch (IOException e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testReaderAccount() {
        jsonReader = new JsonReader("./data/testReaderAccount.json");
        try {
            account = jsonReader.read();
            assertEquals("Test", account.getName());
            assertEquals(2, account.getWallets().size());
            ArrayList<Wallet> wallets = account.getWallets();

            Wallet wallet0 = wallets.get(0);
            ArrayList<Balance> balances0 = wallet0.getBalances();
            checkWallet(wallet0, "Trading", 0, 10);
            checkBalance(balances0.get(0), "ethereum", 10, 101);
            checkBalance(balances0.get(1), "polkadot", 11, 199);

            Wallet wallet1 = wallets.get(1);
            ArrayList<Balance> balances1 = wallet1.getBalances();
            checkWallet(wallet1, "Savings", 1, 19923);
            checkBalance(balances1.get(0), "bitcoin", 2100, 101);
            checkBalance(balances1.get(1), "polkadot", 11, 199);
            checkBalance(balances1.get(2), "ethereum", 10, 199);
        } catch (IOException e) {
            fail("This should not run.");
        }
    }

}
