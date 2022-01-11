package control;

import model.Consumable;
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
 * Generates a custom gson helper for handling json objects
 */

public class GsonHelper {

    public GsonHelper() {
    }

    public Gson getGson() {
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

}
