package json;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter {
    private final Gson gson = new Gson();

    public <T> void write(T object, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(gson.toJson(object));
        writer.close();
    }
}
