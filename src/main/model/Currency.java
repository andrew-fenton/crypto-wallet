package model;

import org.json.JSONObject;

// Represents a currency
public class Currency {
    private String name;
    private double price;

    // EFFECTS: constructs ethereum currency with given price
    public Currency(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // EFFECTS: returns price of currency
    public double getPrice() {
        return this.price;
    }

    // EFFECTS: returns name of currency
    public String getName() {
        return this.name;
    }

    // EFFECTS: returns currency as JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("price", price);

        return json;
    }

    // EFFECTS: returns currency name for JList GUI
    @Override
    public String toString() {
        return name;
    }
}
