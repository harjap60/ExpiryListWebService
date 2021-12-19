package ca.cmpt213.a4.webappserver.model;

import java.time.LocalDateTime;

/**
 * +
 * Sub-class of Consumable, holds extra fields and has a toString method to return all fields from it and its superclass
 */
public class FoodStatistics extends Consumable {
    private long id;
    private double weight;

    /**
     * @param name       - Name of the food item
     * @param notes      - Notes about the food item
     * @param price      - Price of the food item
     * @param expiryDate - LocalDateTime object of the expiry date
     * @param weight     - Weight of the food item
     */
    public FoodStatistics(String name, String notes, double price, LocalDateTime expiryDate, double weight) {
        super(name, notes, price, expiryDate);
        this.weight = weight;
        gsonString = "FoodStatistics";
    }

    public void setId(long andIncrement) {
        id = andIncrement;
    }

    public double getWeight() {
        return weight;
    }

    public long getId() {
        return id;
    }
}
