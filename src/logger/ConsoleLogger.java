package logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements Logger {
    private final LogLevel minLevel;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");


    public ConsoleLogger(LogLevel minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public void log(LogLevel logLevel, String content) {
        if (logLevel.getLevel() >= minLevel.getLevel()) {
            System.out.printf("%9s | %s | %s\n", logLevel, dtf.format(LocalDateTime.now()), content);
        }
    }
}
