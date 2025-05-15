package com.openway.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class for setting up logging for test execution
 */
public class LoggerUtil {
    private static final String LOG_FILE_PATH = "target/logs/test-execution.log";
    private static boolean isInitialized = false;
    
    /**
     * Initialize logger with console and file handlers
     */
    public static void initLogger() {
        if (isInitialized) {
            return;
        }
        
        try {
            Level logLevel = getLogLevel();
            
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(logLevel);
            
            for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }
            
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(logLevel);
            rootLogger.addHandler(consoleHandler);
            
            java.io.File logDir = new java.io.File("target/logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH, true);
            fileHandler.setLevel(logLevel);
            fileHandler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(fileHandler);
            
            isInitialized = true;
            
            Logger logger = Logger.getLogger(LoggerUtil.class.getName());
            logger.info("Logger initialized with level: " + logLevel.getName());
            
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get log level from properties file or default to INFO
     * 
     * @return logging level
     */
    private static Level getLogLevel() {
        String logLevelStr = "INFO"; 
        
        try {
            Properties properties = new Properties();
            FileInputStream input = new FileInputStream("src/test/resources/config.properties");
            properties.load(input);
            
            logLevelStr = properties.getProperty("log.level", "INFO").toUpperCase();
            input.close();
        } catch (IOException e) {
            System.out.println("Could not load log level from properties, using default: " + logLevelStr);
        }
        
        // Convert string to Level
        try {
            return Level.parse(logLevelStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid log level: " + logLevelStr + ", using INFO");
            return Level.INFO;
        }
    }
}