package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EthereumTest {

    private Wallet wallet;
    private Ethereum eth;

    @BeforeEach
    public void runBefore() {
        eth = new Ethereum(10);
        wallet = new Wallet("Test", 1);
    }

    @Test
    public void testConstructor() {
        assertEquals(10, eth.getPrice());
    }

    @Test
    public void testBuy() {
        eth.buy(wallet, 100);
        assertEquals(100, wallet.getEthBalance());
    }

    @Test
    public void testSell() {
        wallet.addEthBalance(100);
        eth.sell(wallet, 100);
        assertEquals(0, wallet.getEthBalance());
    }

    @Test
    public void testGetName() {
        assertEquals(eth.getName(), "Ethereum");
    }
}
