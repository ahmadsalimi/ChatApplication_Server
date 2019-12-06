package models;

public class Response<T> {
    private final ResponseType type;
    private final T content;

    public Response(ResponseType type, T content) {
        this.type = type;
        this.content = content;
    }
}
