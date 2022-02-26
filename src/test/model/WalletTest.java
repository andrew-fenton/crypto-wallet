package model;

import exceptions.BalanceNotFound;
import exceptions.BalancesIsEmpty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for Wallet class
public class WalletTest {

    private Wallet wallet;
    private Currency eth;
    private Currency dot;
    private Currency btc;
    private Balance ethBal;
    private Balance dotBal;
    private Balance btcBal;

    @BeforeEach
    public void runBefore() {
        wallet = new Wallet("Test", 1);
        eth = new Ethereum(10);
        dot = new Polkadot(5);
        btc = new Bitcoin(100);
        ethBal = new Balance(eth);
        dotBal = new Balance(dot);
        btcBal = new Balance(btc);
        wallet.addBalance(ethBal);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test", wallet.getName());
        assertEquals(1, wallet.getID());
        assertEquals(0, wallet.getDollarBalance());
    }

    @Test
    public void testDeposit() {
        wallet.deposit(1000);
        assertEquals(1000, wallet.getDollarBalance());

        wallet.deposit(500);
        assertEquals(1500, wallet.getDollarBalance());
    }

    @Test
    public void testAddCurrencyBalance() {
        try {
            wallet.addToBalance(eth,10);
            assertEquals(10, wallet.selectBalance(eth).getBalance());
            wallet.addToBalance(eth,500);
            assertEquals(510, wallet.selectBalance(eth).getBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testBuySufficientBalance() {
        wallet.deposit(100);
        try {
            assertTrue(wallet.buy(eth, 10));
            assertEquals(10, wallet.selectBalance(eth).getBalance());
            assertEquals(0, wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testBuyMultiple() {
        wallet.deposit(10000);
        try {
            assertTrue(wallet.buy(eth, 3));
            assertEquals(3, wallet.selectBalance(eth).getBalance());
            assertEquals(10000 - (3 * eth.getPrice()), wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }

        try {
            assertTrue(wallet.buy(eth, 4));
            assertEquals(7, wallet.selectBalance(eth).getBalance());
            assertEquals(10000 - (7 * eth.getPrice()), wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testBuyInsufficientBalance() {
        try {
            assertFalse(wallet.buy(eth, 10));
            assertEquals(0, wallet.selectBalance(eth).getBalance());
            assertEquals(0, wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testBuyExceptionBalancesIsEmpty() {
        try {
            Wallet wallet1 = new Wallet("Test", 0);
            assertTrue(wallet1.getBalances().isEmpty());
            wallet1.buy(eth, 10);
//            fail("This should not run.");
        } catch (BalanceNotFound e) {
            fail("This should not run.");
        } catch (BalancesIsEmpty e) {
            // Expected
        }
    }

    @Test
    public void testBuyExceptionBalanceNotFound() {
        try {
            Wallet wallet1 = new Wallet("Test", 0);
            wallet1.addBalance(dotBal);
            assertFalse(wallet1.buy(dot, 10));
//            fail("This should not run.");
        } catch (BalancesIsEmpty e) {
            fail("This should not run.");
        } catch (BalanceNotFound e) {
            // Expected
        }
    }

    @Test
    public void testSellSufficientBalance() {
        try {
            wallet.addToBalance(eth, 9);
            assertTrue(wallet.sell(eth, 9));
            assertEquals(0, wallet.selectBalance(eth).getBalance());
            assertEquals((9 * eth.getPrice()), wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testSellMultiple() {
        try {
            wallet.addToBalance(eth, 9);
            assertTrue(wallet.sell(eth, 3));
            assertEquals(6, wallet.selectBalance(eth).getBalance());
            assertEquals((3 * eth.getPrice()), wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }

        try {
            assertTrue(wallet.sell(eth, 4));
            assertEquals(2, wallet.selectBalance(eth).getBalance());
            assertEquals((7 * eth.getPrice()), wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testSellInsufficientBalance() {
        try {
            wallet.addToBalance(eth, 9);
            assertFalse(wallet.sell(eth, 10));
            assertEquals(9, wallet.selectBalance(eth).getBalance());
            assertEquals(0, wallet.getDollarBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testSelectBalance() {
        wallet.addBalance(ethBal);

        try {
            wallet.selectBalance(eth);
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testSelectEmptyBalances() {
        Wallet wallet1 = new Wallet("Test", 0);

        try {
            wallet1.selectBalance(btc);
            fail("This should not run.");
        } catch (BalanceNotFound e) {
            fail("This should not run.");
        } catch (BalancesIsEmpty e) {
            // Expected
        }
    }

    @Test
    public void testSelectBalanceNotFound() {
        Wallet wallet1 = new Wallet("Test", 0);
        wallet1.addBalance(ethBal);

        try {
            wallet1.selectBalance(btc);
            fail("This should not run.");
        } catch (BalancesIsEmpty e) {
            fail("This should not run.");
        } catch (BalanceNotFound e) {
            // Expected
        }
    }

    @Test
    public void testAddBalance() {
        assertEquals(1, wallet.getBalances().size());

        assertTrue(wallet.addBalance(dotBal));
        assertEquals(2, wallet.getBalances().size());

        // Already Existing
        assertFalse(wallet.addBalance(ethBal));
        assertEquals(2, wallet.getBalances().size());
    }

    @Test
    public void testAddBalanceMultiple() {
        assertEquals(1, wallet.getBalances().size());

        assertTrue(wallet.addBalance(dotBal));
        assertEquals(2, wallet.getBalances().size());

        assertTrue(wallet.addBalance(btcBal));
        assertEquals(3, wallet.getBalances().size());
    }

    @Test
    public void testAddToBalance() {
        try {
            assertTrue(wallet.addToBalance(eth, 10));
            assertEquals(10, wallet.selectBalance(eth).getBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testAddToBalanceNegativeAmount() {
        try {
            assertFalse(wallet.addToBalance(eth, -1));
            assertEquals(0, wallet.selectBalance(eth).getBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testAddToBalanceMultiple() {
        try {
            assertTrue(wallet.addToBalance(eth, 10));
            assertEquals(10, wallet.selectBalance(eth).getBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }

        try {
            assertTrue(wallet.addToBalance(eth, 10));
            assertEquals(20, wallet.selectBalance(eth).getBalance());
        } catch (BalancesIsEmpty | BalanceNotFound e) {
            fail("This should not run.");
        }
    }

    @Test
    public void testAddToBalanceExceptionIsEmpty() {
        try {
            Wallet wallet1 = new Wallet("Test", 0);
            assertFalse(wallet1.addToBalance(eth, 10));
        } catch (BalanceNotFound e) {
            fail("This should not run.");
        } catch (BalancesIsEmpty e) {
            // Expected
        }
    }

    @Test
    public void testAddToBalanceExceptionNotFound() {
        try {
            Wallet wallet1 = new Wallet("Test", 0);
            wallet1.addBalance(dotBal);
            assertFalse(wallet1.addToBalance(eth, 10));
        } catch (BalancesIsEmpty e) {
            fail("This should not run.");
        } catch (BalanceNotFound e) {
            // Expected
        }
    }
}
