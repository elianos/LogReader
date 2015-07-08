package cz.vjinoch.log.reader.tail;

import cz.vjinoch.log.reader.model.DatePattern;
import cz.vjinoch.log.reader.model.LogFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggerRepository;

import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trida zapouzdruje nacteni Loggeru z knihovny Log4j.
 *
 * @author usul
 */
public class Log4jAppenderLoader {

    public static final String PATTERN_PART_FIND_REGEX = "%(-){0,1}\\w*[cCFlLmMnprtxX](\\{.*\\}){0,1}";
    public static final String DATE_PATTERN_FIND_REGEX = "%d\\{([^}]+)\\}";
    public static final String DATE_PATTERN_REPLACE_REGEX = "%d(\\{.*\\}){0,1}";
    private static final Logger LOGGER = Logger.getLogger(Log4jAppenderLoader.class);
    private Map<String, LogFile> appenderMap = new HashMap<String, LogFile>();

    Log4jAppenderLoader() {
        Enumeration<Logger> loggers = LogManager.getCurrentLoggers();
        LoggerRepository loggerRepository = LogManager.getLoggerRepository();

        while (loggers.hasMoreElements()) {
            Logger logger = loggers.nextElement();
            readLogger(logger);
        }

        Logger logger = LogManager.getRootLogger();
        readLogger(logger);


    }

    /**
     * Na zaklade patternu vrati {@link DatePattern}, ktery obsahuje postup jak precist z dat datum kdy doslo k zapisu.
     *
     * @param logPatter Pattern z appenderu.
     * @return {@link DatePattern} pro nacteni datumu.
     */
    public static DatePattern createDatePatter(String logPatter) {
        LOGGER.info("Vytvarim regex pro pattern: " + logPatter);
        logPatter = logPatter.replaceAll("\\(", "\\\\(");
        logPatter = logPatter.replaceAll("\\)", "\\\\)");
        logPatter = logPatter.replaceAll("\\[", "\\\\[");
        logPatter = logPatter.replaceAll("\\]", "\\\\]");
        LOGGER.debug("Byly escapovany kulate zavorky: " + logPatter);

        Pattern pattern = Pattern.compile(DATE_PATTERN_FIND_REGEX);
        Matcher matcher = pattern.matcher(logPatter);
        if (!matcher.find()) {
            LOGGER.error("Pattern neobsahuje vypis datumu!");
            return null;
        }
        String dateFormatter = matcher.group(1);
        LOGGER.debug("Byl nacten pattern pro datum: " + dateFormatter);
        String dateFormatPattern = dateFormatter.replaceAll("[GyYMwWDdFEuaHkKhmsSzZX]{1,}", "(.*)");
        dateFormatPattern = "(" + dateFormatPattern + ")";
        LOGGER.debug("Pro pattern datumu byl vytvoren regex: " + dateFormatPattern);

        String finalPattern = logPatter.replaceAll(PATTERN_PART_FIND_REGEX, "(.*)");
        finalPattern = finalPattern.replaceAll(DATE_PATTERN_REPLACE_REGEX, dateFormatPattern);
        LOGGER.info("Byl vytvoren regex pattern: " + finalPattern);

        LOGGER.debug(Pattern.quote(dateFormatPattern));

        Integer index = readIndex(dateFormatPattern, finalPattern);
        if (index == null) return null;

        return new DatePattern(index, finalPattern, dateFormatter);
    }

    /**
     * Provede nacteni indexu na jakem je v regularnim vyrazu ulozeno datum.
     *
     * @param dateFormat   pattern pro nacteni data.
     * @param finalPattern cely pattern ve kterem bude index vyhledan.
     * @return Index na kterem je v patternu ulozeno datum.
     */
    private static Integer readIndex(String dateFormat, String finalPattern) {
        Integer index;

        String[] parts = finalPattern.split(Pattern.quote(dateFormat));
        if (parts.length < 2) {
            LOGGER.error("Pattern nebosahuje regex s datumem!");
            return null;
        }

        index = StringUtils.countMatches(parts[0], "(.*)");
        index++;
        LOGGER.info("Pattern obsahuje regex s datem na indexu: " + index);
        return index;
    }

    /**
     * Vraci mapu appenderu a referovanych souboru.
     *
     * @return mapa appendru a referenci na soubory.
     */
    public Map<String, LogFile> getAppenderMap() {
        return appenderMap;
    }

    /**
     * Provede nacteni patternu z loggeru.
     *
     * @param logger instance root loggeru.
     */
    private void readLogger(Logger logger) {
        Enumeration<Appender> appenders = logger.getAllAppenders();

        while (appenders.hasMoreElements()) {
            Appender appender = appenders.nextElement();

            if (appender instanceof FileAppender) {

                FileAppender fileAppender = (FileAppender) appender;

                String appenderName = fileAppender.getName();
                String pathname = fileAppender.getFile();

                String logPattern = readDatePattern(fileAppender);

                if (!appenderMap.containsKey(appenderName)) {
                    appenderMap.put(appenderName, new LogFile(new File(pathname), createDatePatter(logPattern)));
                }

            }
        }
    }

    /**
     * Metoda nacte pattern z fileAppenderu.
     *
     * @param fileAppender instance {@link FileAppender}
     * @return pattern pro vypis do logu
     */
    private String readDatePattern(FileAppender fileAppender) {
        String datePattern = null;

        Layout layout = fileAppender.getLayout();
        if (layout instanceof PatternLayout) {
            PatternLayout patternLayout = (PatternLayout) layout;
            datePattern = patternLayout.getConversionPattern();
        }
        if (layout instanceof EnhancedPatternLayout) {
            EnhancedPatternLayout enhancedPatternLayout = (EnhancedPatternLayout) layout;
            datePattern = enhancedPatternLayout.getConversionPattern();
        }
        return datePattern;
    }
}
