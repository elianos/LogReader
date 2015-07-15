package cz.vjinoch.log.reader.appender;

import cz.vjinoch.log.reader.model.LogFile;

import java.util.List;
import java.util.Map;

/**
 * TODO: comment
 *
 * @author vjinoch
 */
public interface IAppenderLoader {

    public Map<String, LogFile> getAppenderMap();

}
