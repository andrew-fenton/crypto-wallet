package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BalanceTest {

    private Balance balance;
    private Ethereum eth;

    @BeforeEach
    public void runBefore() {
        eth = new Ethereum(10);
        balance = new Balance(eth);
    }

    @Test
    public void testConstructor() {
        assertEquals(eth, balance.getCurrency());
        assertEquals(0, balance.getBalance());
    }

    @Test
    public void testIncrementBalance() {
        balance.incrementBalance(100);
        assertEquals(100, balance.getBalance());
    }

    @Test
    public void testIncrementBalanceMultiple() {
        balance.incrementBalance(100);
        assertEquals(100, balance.getBalance());

        balance.incrementBalance(90);
        assertEquals(190, balance.getBalance());
    }

    @Test
    public void testReduceBalance() {
        balance.incrementBalance(100);
        balance.reduceBalance(10);
        assertEquals(90, balance.getBalance());
    }

    @Test
    public void testReduceBalanceMultiple() {
        balance.incrementBalance(100);
        balance.reduceBalance(10);
        assertEquals(90, balance.getBalance());

        balance.reduceBalance(10);
        assertEquals(80, balance.getBalance());
    }

}
