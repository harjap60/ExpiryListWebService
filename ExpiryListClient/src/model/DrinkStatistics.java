package model;

import java.time.LocalDateTime;
/**
 * Sub-class of Consumable, holds extra fields and has a toString method to return all fields from it and its superclass
 */

public class DrinkStatistics extends Consumable {
    private final double volume;
    private long id;

    /**
     * @param name       - Name of the drink item
     * @param notes      - Notes about the drink item
     * @param price      - Price of the drink item
     * @param volume     - Volume of drink item
     * @param expiryDate - LocalDateTime object of the expiry date
     */
    public DrinkStatistics(String name, String notes, double price, LocalDateTime expiryDate, double volume) {
        super(name, notes, price, expiryDate);
        this.volume = volume;
        gsonString = "DrinkStatistics";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return returns a string of the current items fields(used by TextUI)
     */
    @Override
    public String toString() {
        String expired = ("\nThis drink will expire in " + Math.abs(daysExpired()) + " days(s).");
        // Checks if item is expired to return the better corresponding string
        if (isExpired()) {
            expired = ("\nThis drink is expired for " + Math.abs(daysExpired()) + " days(s).");
        }

        return ("Name: " + getName() + "<br>Notes: " + getNotes() + "<br>Price: $" + getPrice() +
                "<br>Volume: " + volume + "<br>Expiry Date: " + getExpiryDate() + "<br>" + expired + "</html>");
    }

}
