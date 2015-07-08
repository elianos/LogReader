package cz.vjinoch.log.reader.tail;

import cz.vjinoch.log.reader.model.DatePattern;
import cz.vjinoch.log.reader.model.LogFile;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class Log4jAppenderLoaderTest {

    private static final Logger LOGGER = Logger.getLogger(Log4jAppenderLoaderTest.class);

    @Test
    public void test() throws IOException, ParseException {

        Log4jAppenderLoader log4jAppenderLoader = new Log4jAppenderLoader();
        Map<String, LogFile> appenderMap = log4jAppenderLoader.getAppenderMap();

        for (String appenderName : appenderMap.keySet()) {
            LOGGER.info(appenderName);
            LogFile logFile = appenderMap.get(appenderName);

            LOGGER.info(logFile);
        }
    }


    @Test
    public void createDatePatterTest() throws ParseException {
        String patter = "[%-5p] (%d{yyyy-MM-dd HH:mm:ss}) %c{1}:%L - %m%n";

        DatePattern datePattern = Log4jAppenderLoader.createDatePatter(patter);

        LOGGER.info(datePattern.readDate("[DEBUG] (2015-06-22 20:24:06) Log4jAppenderLoaderTest:14 - Log aaaaaaaa"));

    }
}
