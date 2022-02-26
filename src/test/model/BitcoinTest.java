package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BitcoinTest {

    private Wallet wallet;
    private Bitcoin btc;
    private Balance btcBal;

    @BeforeEach
    public void runBefore() {
        btc = new Bitcoin(10);
        wallet = new Wallet("Test", 1);
        btcBal = new Balance(btc);
        wallet.addBalance(btcBal);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, btc.getPrice());
    }

    @Test
    public void testGetName() {
        assertEquals("Bitcoin", btc.getName());
    }

}
