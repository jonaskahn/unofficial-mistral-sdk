package one.ifelse.tools.mistral2;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

public class Log4jConfigurator {

  public static void configure() {
    Logger rootLogger = Logger.getRootLogger();
    rootLogger.setLevel(Level.INFO);

    // Define the pattern layout with ANSI colors and thread name
    PatternLayout layout = new PatternLayout() {
      private static final String ANSI_RESET = "\u001B[0m";
      private static final String ANSI_RED = "\u001B[31m";
      private static final String ANSI_GREEN = "\u001B[32m";
      private static final String ANSI_YELLOW = "\u001B[33m";
      private static final String ANSI_BLUE = "\u001B[34m";
      private static final String ANSI_CYAN = "\u001B[36m"; // Color for TRACE level

      @Override
      public String format(LoggingEvent event) {
        String levelColor;
        switch (event.getLevel().toInt()) {
          case Level.ERROR_INT:
            levelColor = ANSI_RED;
            break;
          case Level.WARN_INT:
            levelColor = ANSI_YELLOW;
            break;
          case Level.INFO_INT:
            levelColor = ANSI_GREEN;
            break;
          case Level.DEBUG_INT:
            levelColor = ANSI_BLUE;
            break;
          case Level.TRACE_INT:
            levelColor = ANSI_CYAN;
            break;
          default:
            levelColor = ANSI_RESET;
        }

        // Original pattern: "%d{HH:mm:ss} %-5p [%t] %c{1} - %m%n"
        return String.format("%s%s %-5s [%s] %s - %s%n%s",
            levelColor,
            event.getTimeStamp(),
            event.getLevel(),
            event.getThreadName(),
            event.getLoggerName(),
            event.getRenderedMessage(),
            ANSI_RESET);
      }
    };

    // Define a console appender
    ConsoleAppender consoleAppender = new ConsoleAppender(layout);
    rootLogger.addAppender(consoleAppender);

    // Optionally, set a specific logger level
    Logger appLogger = Logger.getLogger("one.ifelse.tools");
    appLogger.setLevel(Level.ALL);
  }
}
