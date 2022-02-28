package ui;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new WalletApp();
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        }
    }
}
