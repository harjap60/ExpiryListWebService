package server.model;

import java.time.LocalDateTime;

/**
 * Sub-class of Consumable, holds extra fields and has a toString method to return all fields from it and its superclass
 */

public class DrinkStatistics extends Consumable {
    private double volume;
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

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
