package cz.vjinoch.log.reader.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trida umoznuje nacist datum ze zaznamu loggeru.
 * <p>
 * Created by usul on 8.7.2015.
 */
public class DatePattern {

    private final String datePattern;

    private final Integer index;

    private final String dateFormatter;

    private final Pattern pattern;

    private final DateFormat dateFormat;

    public DatePattern(Integer index, String datePattern, String dateFormatter) {
        this.index = index;
        this.datePattern = datePattern;
        this.dateFormatter = dateFormatter;
        this.pattern = Pattern.compile(datePattern);
        this.dateFormat = new SimpleDateFormat(dateFormatter);
    }

    /**
     * Metoda provadi nacteni datumu z retezce odpovidajici patternu pro danny log.
     *
     * @param data Retezec radku logu.
     * @return <b>null</b> - pokud radek neobsahuje datum; Jinak vraci datum kdy doslo k zalogovani.
     * @throws ParseException
     */
    public Date readDate(String data) throws ParseException {
        Matcher matcher = pattern.matcher(data);
        if (!matcher.find()) {
            return null;
        }

        String dateString = matcher.group(index);
        return dateFormat.parse(dateString);
    }

    @Override
    public String toString() {
        return "DatePattern : { " +
                "datePattern : '" + datePattern + '\'' +
                ", index : " + index +
                ", dateFormatter : '" + dateFormatter + '\'' +
                " }";
    }
}
