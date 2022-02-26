package model;

public class Polkadot implements Currency {

    private String name;
    private double price;

    public Polkadot(double price) {
        this.name = "Polkadot";
        this.price = price;
    }

    // EFFECTS: returns name
    @Override
    public String getName() {
        return name;
    }

    // EFFECTS: returns price
    @Override
    public double getPrice() {
        return price;
    }
}
