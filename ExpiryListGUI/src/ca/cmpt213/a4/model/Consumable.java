package ca.cmpt213.a4.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
/** +
 * (Superclass) for FoodStatistics and DrinkStatistics
 * Class stores fields for consumable items and deals with everything to do with expiry dates for it.
 */

public class Consumable implements Comparable {
    private String name;
    private String notes;
    private double price;
    private LocalDateTime expiry;
    protected String gsonString = "Consumable"; // Used for GSON saving/loading

    public Consumable(String name, String notes,
                      double price, LocalDateTime expiry) {
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.expiry = expiry;
    }

    public String getGsonString() {
        return gsonString;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    /**
     * @return returns the (LocalDate)expiry date of the current food item
     */
    public LocalDate getExpiryDate() {

        return getExpiry().toLocalDate();
    }

    /**
     * @return Returns true if the current food item is expired, false if it's not expired
     */
    public boolean isExpired() {
        //Gets current date
        LocalDate now = LocalDate.now();
        // Gets the difference between the current date and current food item expiry date
        Duration diff = Duration.between(now.atStartOfDay(), getExpiry().toLocalDate().atStartOfDay());

        return diff.toDays() < 0;
    }

    /**
     * @return Returns the number of days the current item is expired for
     */
    public int daysExpired() {
        LocalDate now = LocalDate.now();
        Duration diff = Duration.between(now.atStartOfDay(), getExpiry().toLocalDate().atStartOfDay());
        return (int) diff.toDays();
    }

    /**
     * @return - returns 1 if current object has expiry date before the other object. Returns 0 if expiry date is after
     */
    @Override
    public int compareTo(Object o) {
        Consumable object = (Consumable) o;
        if (getExpiryDate().isBefore(object.getExpiryDate())) {
            return 1;
        }
        return 0;
    }

}
