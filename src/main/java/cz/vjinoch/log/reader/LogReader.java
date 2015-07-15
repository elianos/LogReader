package cz.vjinoch.log.reader;

import cz.vjinoch.log.reader.appender.log4j.Log4jAppenderLoader;
import cz.vjinoch.log.reader.model.LogFile;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

import java.util.Map;

/**
 * Created by usul on 14.7.2015.
 */
public class LogReader {

    private static final Logger LOGGER = Logger.getLogger(LogReader.class);

    private static LogReader instance;

    private boolean loggerRead = false;

    private Map<String, LogFile> logFileMap;

    private LogReader() {
    }

    public static LogReader getInstance() {
        LOGGER.info("Ziskavam instanci LogReaderu.");
        if (instance == null) {
            LOGGER.info("Prvni inicializace LogReaderu");
            instance = new LogReader();
        }

        return instance;
    }

    public void loadLog4j(LoggerRepository loggerRepository) {
        if (loggerRead == false) {
            Log4jAppenderLoader log4jAppenderLoader = new Log4jAppenderLoader(loggerRepository);
            logFileMap.putAll(log4jAppenderLoader.getAppenderMap());
            loggerRead = true;
            LOGGER.info("Byla inicializovan logger!");
        } else {
            LOGGER.warn("Logger byl jiz inicializovan!");
        }

    }



}
