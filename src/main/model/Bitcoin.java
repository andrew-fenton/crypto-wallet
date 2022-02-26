package model;

public class Bitcoin implements Currency {

    private String name;
    private double price;

    public Bitcoin(double price) {
        this.name = "Bitcoin";
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getName() {
        return name;
    }
}
