package org.framework.Utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Utility class for logging using Log4j2.
 * <p>
 * This class provides static methods for logging messages at various levels
 * (info, debug, warn, error, trace, fatal) without the need to obtain a Logger instance
 * in every class. It is designed as a final utility class with a private constructor.
 * </p>
 */
public final class LoggerUtil {

    // Logger instance for this utility class.
    private static final Logger logger = LogManager.getLogger(LoggerUtil.class);

    // Private constructor to prevent instantiation.
    private LoggerUtil() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    /**
     * Logs an informational message.
     *
     * @param message the message to log
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Logs an error message.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        logger.error(message);
    }

    /**
     * Logs a debug message.
     *
     * @param message the message to log
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    /**
     * Logs a warning message.
     *
     * @param message the message to log
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * Logs a trace message.
     *
     * @param message the message to log
     */
    public static void trace(String message) {
        logger.trace(message);
    }

    /**
     * Logs a fatal error message.
     *
     * @param message the message to log
     */
    public static void fatal(String message) {
        logger.fatal(message);
    }

    /**
     * Logs an error message along with an associated throwable.
     *
     * @param message   the message to log
     * @param throwable the throwable to log
     */
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
}
