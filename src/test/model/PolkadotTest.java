package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolkadotTest {

    private Wallet wallet;
    private Polkadot dot;
    private Balance dotBal;

    @BeforeEach
    public void runBefore() {
        dot = new Polkadot(10);
        wallet = new Wallet("Test", 1);
        dotBal = new Balance(dot);
        wallet.addBalance(dotBal);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, dot.getPrice());
    }

    @Test
    public void testGetName() {
        assertEquals("Polkadot", dot.getName());
    }

}
