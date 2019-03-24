package utils;

import com.google.common.flogger.FluentLogger;

public class Logger {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public static FluentLogger getLog() {
        return log;
    }
}
