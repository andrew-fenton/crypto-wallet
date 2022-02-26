package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for Ethereum class
public class EthereumTest {

    private Wallet wallet;
    private Ethereum eth;
    private Balance ethBal;

    @BeforeEach
    public void runBefore() {
        eth = new Ethereum(10);
        wallet = new Wallet("Test", 1);
        ethBal = new Balance(eth);
        wallet.addBalance(ethBal);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, eth.getPrice());
    }

    @Test
    public void testGetName() {
        assertEquals(eth.getName(), "Ethereum");
    }

}
