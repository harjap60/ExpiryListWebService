package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.control.ConsumablesTracker;
import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.DrinkStatistics;
import ca.cmpt213.a4.webappserver.model.FoodStatistics;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsumablesController {
    private ArrayList<Consumable> consumableArrayList = null;
    private AtomicLong nextId = new AtomicLong();

    @GetMapping("/ping")
    public String getPingMessage() {
        consumableArrayList = ConsumableManager.getInstance().getConsumableArrayList();
        if ( consumableArrayList == null) {
            consumableArrayList = new ArrayList<>();
        }
        return "System is up!";
    }

    @GetMapping("/exit")
    public void exitProgram() {
        ConsumablesTracker save = new ConsumablesTracker();
        // save to ItemsList.json
        try {
            save.saveToJson(consumableArrayList,save);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/addFood")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodStatistics addFood(@RequestBody FoodStatistics foodItem) {
        foodItem.setId(nextId.getAndIncrement());
        consumableArrayList.add(foodItem);
        return foodItem;
    }

    @PostMapping("/addDrink")
    @ResponseStatus(HttpStatus.CREATED)
    public DrinkStatistics addDrink(@RequestBody DrinkStatistics drinkItem) {
        drinkItem.setId(nextId.getAndIncrement());
        consumableArrayList.add(drinkItem);
        return drinkItem;
    }

    @PostMapping("/removeItem/{id}")
    public List<Consumable> removeConsumableItem(@PathVariable("id") long itemId) {
        for (Consumable item : consumableArrayList) {
            if (item instanceof FoodStatistics i) {
                if (i.getId() == itemId) {
                    consumableArrayList.remove(item);
                    return consumableArrayList;
                }
            } else {
                DrinkStatistics j = (DrinkStatistics) item;
                if (j.getId() == itemId) {
                    consumableArrayList.remove(item);
                    return consumableArrayList;
                }
            }
        }
        return consumableArrayList;
        //throw new IllegalArgumentException();
    }

    @GetMapping("/listAll")
    public List<Consumable> listAllConsumables() {
        return consumableArrayList;

    }

    @GetMapping("/listExpired")
    public List<Consumable> listExpiredConsumables() {
        List<Consumable> expiredList = new ArrayList<>();
        for (Consumable item : consumableArrayList) {
            if (item.checkExpired()) {
                expiredList.add(item);
            }
        }
        return expiredList;
    }

    @GetMapping("/listNonExpired")
    public List<Consumable> listNonExpiredConsumables() {
        List<Consumable> expiredList = new ArrayList<>();
        for (Consumable item : consumableArrayList) {
            if (!item.checkExpired()) {
                expiredList.add(item);
            }
        }
        return expiredList;

    }

    @GetMapping("/listExpiringIn7Days")
    public List<Consumable> listExpiringIn7Days() {
        List<Consumable> expiredList = new ArrayList<>();
        for (Consumable item : consumableArrayList) {
            if (!item.checkExpired() && item.daysExpired() <= 7) {
                expiredList.add(item);
            }
        }
        return expiredList;
    }

    // Create Exception Handle
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "Request ID not found.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler() {
    }
}
