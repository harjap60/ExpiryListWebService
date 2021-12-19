package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.DrinkStatistics;
import ca.cmpt213.a4.webappserver.model.FoodStatistics;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * +
 * Custom serializer to help deal with saving sub-classes to Json Files
 * Some help from https://stackoverflow.com/questions/19588020/gson-serialize-a-list-of-polymorphic-objects
 */
public class SubclassSerializer implements JsonSerializer<ArrayList<Consumable>> {

    private static Map<String, Class> map = new TreeMap<String, Class>();

    static {
        map.put("Consumable", Consumable.class);
        map.put("FoodStatistics", FoodStatistics.class);
        map.put("DrinkStatistics", DrinkStatistics.class);
    }

    @Override
    public JsonElement serialize(ArrayList<Consumable> src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        if (src == null)
            return null;
        else {
            JsonArray array = new JsonArray();
            for (Consumable bc : src) {
                Class c = map.get(bc.getGsonString());
                if (c == null)
                    throw new RuntimeException("Unknown class: " + bc.getGsonString());
                array.add(context.serialize(bc, c));

            }
            return array;
        }
    }
}