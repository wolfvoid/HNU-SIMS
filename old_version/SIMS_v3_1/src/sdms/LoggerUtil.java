package sdms;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {
    private static final Level LOG_LEVEL = Level.INFO; // 设置日志级别

    // 静态块，在类加载时执行，用于配置日志记录器
    static {
        configureGlobalLogger();
    }

    // 配置全局日志记录器
    private static void configureGlobalLogger() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(LOG_LEVEL);

        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(LOG_LEVEL);
        rootLogger.addHandler(consoleHandler);
    }

    // 获取给定类的日志记录器
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(LOG_LEVEL);
        return logger;
    }
}
