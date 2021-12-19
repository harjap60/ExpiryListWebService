package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * +
 * Instantiates main objects for the system to work.
 * Loads and saves arrayList
 */

public class ConsumablesTracker {
    private ArrayList<Consumable> consumableArrayList;

    public ConsumablesTracker() {
        consumableArrayList = new ArrayList<>();

    }

    public ArrayList<Consumable> getConsumableArrayList() {
        return consumableArrayList;
    }

    private Gson getGson() {
        GsonBuilder myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).setPrettyPrinting();

        Type type = new TypeToken<ArrayList<Consumable>>() {
        }.getType();
        myGson.registerTypeAdapter(type, new SubclassSerializer());
        myGson.registerTypeAdapter(type, new SubclassDeserializer());


        return myGson.create();
    }

    public void loadFromJson() throws FileNotFoundException {
        Gson gson = getGson();
        File file = new File("./src/itemsList.json");
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader("./src/itemsList.json"));
            Type type = new TypeToken<ArrayList<Consumable>>() {
            }.getType();
            ArrayList<Consumable> testConsumable = new ArrayList<>();
            testConsumable = gson.fromJson(br, type);
            consumableArrayList = testConsumable;
        }
    }

    public void saveToJson(ArrayList<Consumable> array, ConsumablesTracker setup) throws IOException {
        Gson myGson = setup.getGson();
        FileWriter fileWriter = new FileWriter("./src/itemsList.json");
        myGson.toJson(array, fileWriter);
        fileWriter.close();
    }
}
