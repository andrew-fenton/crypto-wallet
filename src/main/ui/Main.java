package ui;

import org.json.JSONException;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            new WalletApp();
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        } catch (JSONException e) {
            System.out.println("There was an issue loading your previous account.");
        }
    }
}
