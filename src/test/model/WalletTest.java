package model;

import model.Currency;
import model.Ethereum;
import model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WalletTest {

    private Wallet wallet;
    private Currency eth;

    @BeforeEach
    public void runBefore() {
        wallet = new Wallet("Test", 1);
        eth = new Ethereum(10);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test", wallet.getName());
        assertEquals(1, wallet.getID());
        assertEquals(0, wallet.getTotalBalance());
    }

    @Test
    public void testAddBalance() {
        wallet.addBalance(1000);
        assertEquals(1000, wallet.getTotalBalance());

        wallet.addBalance(500);
        assertEquals(1500, wallet.getTotalBalance());
    }

    @Test
    public void testAddEthBalance() {
        wallet.addEthBalance(10);
        assertEquals(10, wallet.getEthBalance());

        wallet.addEthBalance(500);
        assertEquals(510, wallet.getEthBalance());
    }

    @Test
    public void testBuySufficientBalance() {
        wallet.addBalance(100);
        assertTrue(wallet.buy(eth, 10));
        assertEquals(10, wallet.getEthBalance());
        assertEquals(0, wallet.getTotalBalance());
    }

    @Test
    public void testBuyMultiple() {
        wallet.addBalance(10000);
        assertTrue(wallet.buy(eth, 3));
        assertEquals(3, wallet.getEthBalance());
        assertEquals(10000 - (3 * eth.getPrice()), wallet.getTotalBalance());

        assertTrue(wallet.buy(eth, 4));
        assertEquals(7, wallet.getEthBalance());
        assertEquals(10000 - (7 * eth.getPrice()), wallet.getTotalBalance());
    }

    @Test
    public void testBuyInsufficientBalance() {
        assertFalse(wallet.buy(eth, 10));
        assertEquals(0, wallet.getEthBalance());
        assertEquals(0, wallet.getTotalBalance());
    }

    @Test
    public void testSellSufficientBalance() {
        wallet.addEthBalance(9);
        assertTrue(wallet.sell(eth, 9));
        assertEquals(0, wallet.getEthBalance());
        assertEquals((9 * eth.getPrice()), wallet.getTotalBalance());
    }

    @Test
    public void testSellMultiple() {
        wallet.addEthBalance(9);
        assertTrue(wallet.sell(eth, 3));
        assertEquals(6, wallet.getEthBalance());
        assertEquals((3 * eth.getPrice()), wallet.getTotalBalance());

        assertTrue(wallet.sell(eth, 4));
        assertEquals(2, wallet.getEthBalance());
        assertEquals((7 * eth.getPrice()), wallet.getTotalBalance());
    }

    @Test
    public void testSellInsufficientBalance() {
        wallet.addEthBalance(9);
        assertFalse(wallet.sell(eth, 10));
        assertEquals(9, wallet.getEthBalance());
        assertEquals(0, wallet.getTotalBalance());
    }

}
