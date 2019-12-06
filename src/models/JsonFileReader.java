package models;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonFileReader<T> {
    private final Gson gson = new Gson();
    private final String filePath;
    private final Class<T> classOfT;

    public JsonFileReader(String filePath, Class<T> classOfT) {
        this.filePath = filePath;
        this.classOfT = classOfT;
    }

    public T read() throws FileNotFoundException {
        return gson.fromJson(new FileReader(new File(filePath)), classOfT);
    }
}
