package json;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonFileReader {
    private final Gson gson = new Gson();

    public <T> T read(File file, Class<T> classOfT) throws FileNotFoundException {
        return gson.fromJson(new FileReader(file), classOfT);
    }

    public <T> T read(String filePath, Class<T> classOfT) throws FileNotFoundException {
        return read(new File(filePath), classOfT);
    }
}
