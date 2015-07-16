package ${package}.logging;

import java.io.Serializable;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author VEDRAX SAS
 */
public class Logger implements Serializable {

    public static final String DEFAULT_LOGGER_NAME = "GenericLogger";
    private static final long serialVersionUID = 1L;
    private transient org.apache.logging.log4j.Logger logger;
    private String loggerName = DEFAULT_LOGGER_NAME;

    public Logger() {
        this(DEFAULT_LOGGER_NAME);
    }

    public Logger(String loggerName) {
        this.loggerName = loggerName;
        initLogger();
    }

    private void initLogger() {
        if (loggerName == null) {
            loggerName = DEFAULT_LOGGER_NAME;
        }
        logger = LogManager.getLogger(loggerName);
    }

    public String getLoggerName() {
        return loggerName;
    }

    private org.apache.logging.log4j.Logger getLogger() {
        if (logger == null) {
            initLogger();
        }
        return logger;
    }

    public void error(String messageFormat, Object... argArray) {
        getLogger().error(messageFormat, argArray);
    }

    public boolean isInfo() {
        return getLogger().isInfoEnabled();
    }

    public void info(String messageFormat, Object... argArray) {
        getLogger().info(messageFormat, argArray);
    }

    public boolean isWarn() {
        return getLogger().isWarnEnabled();
    }

    public void warn(String messageFormat, Object... argArray) {
        getLogger().warn(messageFormat, argArray);
    }

    public boolean isDebug() {
        return getLogger().isDebugEnabled();
    }

    public void debug(String messageFormat, Object... argArray) {
        getLogger().debug(messageFormat, argArray);
    }
}
