package model;

import java.time.LocalDateTime;
/**
 * +
 * Sub-class of Consumable, holds extra fields and has a toString method to return all fields from it and its superclass
 */
public class FoodStatistics extends Consumable {
    private long id;
    private final double weight;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @param name       - Name of the food item
     * @param notes      - Notes about the food item
     * @param price      - Price of the food item
     * @param weight     - Weight of the food item
     * @param expiryDate - LocalDateTime object of the expiry date
     */
    public FoodStatistics(String name, String notes, double price, LocalDateTime expiryDate, double weight) {
        super(name, notes, price, expiryDate);
        this.weight = weight;
        gsonString = "FoodStatistics";
    }

    /**
     * @return returns a string of all current items fields
     */
    @Override
    public String toString() {
        String expired = ("\nThis food will expire in " + Math.abs(daysExpired()) + " days(s).");
        // Checks if item is expired to return the better corresponding string
        if (isExpired()) {
            expired = ("\nThis food is expired for " + Math.abs(daysExpired()) + " days(s).");
        }

        return ("Name: " + getName() + "<br>\nNotes: " + getNotes() + "<br>\nPrice: $" + getPrice() +
                "<br>\nWeight: " + weight + "<br>\nExpiry Date: " + getExpiryDate() + "<br>" + expired + "</html>");
    }

}