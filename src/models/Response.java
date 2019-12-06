package models;

import com.google.gson.Gson;

public class Response<T> {
    private static final Gson gson = new Gson();
    private final ResponseType type;
    private final T content;

    public Response(ResponseType type, T content) {
        this.type = type;
        this.content = content;
    }

    public String toJson() {
        return gson.toJson(this);
    }
}
