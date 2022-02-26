package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account account;
    private Wallet wallet;

    @BeforeEach
    public void runBefore() {
        account = new Account("Test");
        wallet = new Wallet("Test", 1);
    }

    @Test
    public void testConstructor() {
        assertEquals("Test", account.getName());
        assertTrue(account.getWallets().isEmpty());
    }

    @Test
    public void testAddWallet() {
        assertTrue(account.addWallet(wallet));
        assertEquals(1, account.getWallets().size());
    }

    @Test
    public void testAddWalletMultiple() {
        assertTrue(account.addWallet(wallet));
        assertEquals(1, account.getWallets().size());

        assertTrue(account.addWallet(wallet));
        assertEquals(2, account.getWallets().size());
    }

//    @Test
//    public void testGetWallet() {
//        account.addWallet(wallet);
//        assertEquals(wallet, account.getWallet(1));
//
//        account.addWallet(new Wallet("Test", 2));
//        assertEquals(new Wallet("Test", 2), account.getWallet(2));
//    }

    @Test
    public void testGetWallets() {
        ArrayList<Wallet> wallets2 = new ArrayList();
        wallets2.add(wallet);
        wallets2.add(wallet);

        account.addWallet(wallet);
        account.addWallet(wallet);

        assertEquals(wallets2, account.getWallets());
    }



}
