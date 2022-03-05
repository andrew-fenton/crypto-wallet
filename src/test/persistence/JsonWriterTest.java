package persistence;

import model.Account;
import model.Balance;
import model.Currency;
import model.Wallet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// Used code from:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest extends JsonTest {

    private Account account;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    @Test
    void testWriterInvalidFilePath() {
        try {
            account = new Account("Test");
            jsonWriter = new JsonWriter("./data/\0fileName.json");
            jsonWriter.open();
            fail("This should not run.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    void testWriterEmptyAccount() {
        try {
            account = new Account("Test");
            jsonWriter = new JsonWriter("./data/testWriterEmptyAccount.json");
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();

            jsonReader = new JsonReader("./data/testWriterEmptyAccount.json");
            account = jsonReader.read();
            assertEquals("Test", account.getName());
            assertEquals(0, account.getWallets().size());

        } catch (IOException e) {
            fail("This should not run.");
        }
    }

    @Test
    void testWriterAccount() {
        Currency ethereum = new Currency("ethereum", 10);
        Currency polkadot = new Currency("polkadot", 11);
        Currency bitcoin = new Currency("bitcoin", 2100);

        Balance ethBal = new Balance(ethereum);
        ethBal.incrementBalance(101);
        Balance ethBal2 = new Balance(ethereum);
        ethBal2.incrementBalance(199);
        Balance dotBal = new Balance(polkadot);
        dotBal.incrementBalance(199);
        Balance btcBal = new Balance(bitcoin);
        btcBal.incrementBalance(101);

        Wallet initWallet = new Wallet("Trading", 0);
        initWallet.deposit(10);
        initWallet.addBalance(ethBal);
        initWallet.addBalance(dotBal);

        Wallet initWallet1 = new Wallet("Savings", 1);
        initWallet1.deposit(19923);
        initWallet1.addBalance(btcBal);
        initWallet1.addBalance(dotBal);
        initWallet1.addBalance(ethBal2);

        try {
            account = new Account("Test");
            account.addWallet(initWallet);
            account.addWallet(initWallet1);

            jsonWriter = new JsonWriter("./data/testWriterAccount.json");
            jsonWriter.open();
            jsonWriter.write(account);
            jsonWriter.close();

            jsonReader = new JsonReader("./data/testWriterAccount.json");
            account = jsonReader.read();
            assertEquals("Test", account.getName());
            assertEquals(2, account.getWallets().size());
            ArrayList<Wallet> wallets = account.getWallets();

            Wallet wallet0 = wallets.get(0);
            ArrayList<Balance> balances0 = wallet0.getBalances();
            assertEquals(2, balances0.size());
            checkWallet(wallet0, "Trading", 0, 10);
            checkBalance(balances0.get(0), "ethereum", 10, 101);
            checkBalance(balances0.get(1), "polkadot", 11, 199);

            Wallet wallet1 = wallets.get(1);
            ArrayList<Balance> balances1 = wallet1.getBalances();
            assertEquals(3, balances1.size());
            checkWallet(wallet1, "Savings", 1, 19923);
            checkBalance(balances1.get(0), "bitcoin", 2100, 101);
            checkBalance(balances1.get(1), "polkadot", 11, 199);
            checkBalance(balances1.get(2), "ethereum", 10, 199);
        } catch (IOException e) {
            fail("This should not run.");
        }
    }

}
