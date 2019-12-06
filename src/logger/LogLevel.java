package logger;

public enum LogLevel {
    Info(0),
    Warning(1),
    Error(2);

    private final int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
