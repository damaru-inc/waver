package com.damaru.waver;

/**
 * The worlds simplest logger.
 */
public class Log {

    public static void log(String format, Object... args) {

        System.out.printf(format + "\n", args);
    }
}
