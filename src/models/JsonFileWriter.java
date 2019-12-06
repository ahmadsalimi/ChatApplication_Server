package models;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter<T> {
    private final Gson gson = new Gson();
    private final String filePath;

    public JsonFileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void write(T object) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(gson.toJson(object));
        writer.close();
    }
}
