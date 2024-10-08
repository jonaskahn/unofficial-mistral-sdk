package one.ifelse.tools;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4jConfigurator {

  public static void configure() {
    Logger rootLogger = Logger.getRootLogger();
    rootLogger.setLevel(Level.INFO);

    // Define the pattern layout for the console output
    PatternLayout layout = new PatternLayout("%d{HH:mm:ss} %-5p [%t] %c{1} - %m%n");

    // Define a console appender
    ConsoleAppender consoleAppender = new ConsoleAppender(layout);
    rootLogger.addAppender(consoleAppender);

    // Optionally, set a specific logger level
    Logger appLogger = Logger.getLogger("one.ifelse.tools");
    appLogger.setLevel(Level.ALL);
  }
}

