package cz.vjinoch.log.reader.model;

import java.io.File;

/**
 * Trida reprezentuje model reference na log soubor a pattern pro nacitani datumu.
 *
 * @author vjinoch
 */
public class LogFile {

    /**
     * Cesta k log souboru
     */
    private final File file;

    /**
     * Pattern pro vyhledani data z loggeru
     */
    private DatePattern datePattern;

    public LogFile(File file, DatePattern datePattern) {
        super();
        this.file = file;
        this.datePattern = datePattern;
    }

    @Override
    public String toString() {
        return "LogFile [file=" + file + ", datePattern=" + datePattern + "]";
    }

    public DatePattern getDatePattern() {
        return datePattern;
    }

    public File getFile() {
        return file;
    }
}
