package server.control;

import server.model.Consumable;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * +
 * Manager gets and stores item from the itemsList.json file
 */
public class ConsumableManager {
    private static ConsumableManager instance = null;

    private ArrayList<Consumable> consumableArrayList = null;

    private ConsumableManager() {
        ConsumablesTracker setup = new ConsumablesTracker();
        // Load from ItemsList.json
        try {
            setup.loadFromJson();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        consumableArrayList = setup.getConsumableArrayList();
    }

    public static ConsumableManager getInstance() {
        if (instance == null)
            instance = new ConsumableManager();

        return instance;
    }

    public ArrayList<Consumable> getConsumableArrayList() {
        return consumableArrayList;
    }
}
