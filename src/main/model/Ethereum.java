package model;

// Represents Ethereum currency
public class Ethereum implements Currency {

    private String name;
    private double price;

    // EFFECTS: constructs ethereum currency with given price
    public Ethereum(double price) {
        this.name = "Ethereum";
        this.price = price;
    }

    @Override
    // EFFECTS: returns price of currency
    public double getPrice() {
        return this.price;
    }

    @Override
    // EFFECTS: returns name of currency
    public String getName() {
        return this.name;
    }
}
