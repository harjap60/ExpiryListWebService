package ca.cmpt213.a4.control;

import ca.cmpt213.a4.model.Consumable;
import ca.cmpt213.a4.model.DrinkStatistics;
import ca.cmpt213.a4.model.FoodStatistics;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * +
 * Custom deserializer to help deal with loading sub-classes from Json Files
 * Some help from https://stackoverflow.com/questions/19588020/gson-serialize-a-list-of-polymorphic-objects
 */
public class SubclassDeserializer implements JsonDeserializer<List<Consumable>> {

    private static Map<String, Class> map = new TreeMap<String, Class>();

    static {
        map.put("BaseClass", Consumable.class);
        map.put("FoodStatistics", FoodStatistics.class);
        map.put("DrinkStatistics", DrinkStatistics.class);
    }

    public List<Consumable> deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {

        List list = new ArrayList<Consumable>();
        JsonArray ja = json.getAsJsonArray();

        for (JsonElement je : ja) {
            String type = je.getAsJsonObject().get("gsonString").getAsString();
            Class c = map.get(type);
            if (c == null)
                throw new RuntimeException("Unknown class: " + type);
            list.add(context.deserialize(je, c));
        }
        return list;
    }
}