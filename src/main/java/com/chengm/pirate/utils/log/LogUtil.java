package com.chengm.pirate.utils.log;

import com.chengm.pirate.config.AppConfig;
import com.chengm.pirate.utils.CollectionsUtil;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * program: CmPirate
 * description: log 工具类
 * author: ChengMo
 * create: 2019-11-30 22:10
 **/
public class LogUtil {

    private static org.slf4j.Logger mLogger = LoggerFactory.getLogger(LogUtil.class);

    private final static String TAG = "TAG";

    private static int mLevel = LogLevel.LEVEL_TRACE;

    private LogUtil() {}

    /**
     * 不管是不是debug都会打印日志
     */
    public static void logger(String tag, String value, @LogLevel int level) {
        writeLog(tag, value, level);
    }

    /**
     * 打印日志
     */
    public static void logValue(String value) {
        logValue(TAG, value, mLevel);
    }

    /**
     * 打印日志
     */
    public static void logValue(String tag, String value) {
        logValue(tag, value, mLevel);
    }

    /**
     * 打印日志
     */
    public static void logValue(String value, @LogLevel int level) {
        logValue(TAG, value, level);
    }

    /**
     * 打印日志
     */
    public static void logValue(String tag, String value, @LogLevel int level) {
        if (!isEnable()) return;
        writeLog(tag, value, level);
    }

    /**
     * 打印请求参数
     */
    public static void logParam(Map<String, Object> map) {
        logParam(TAG, map, mLevel);
    }

    /**
     * 打印请求参数
     */
    public static void logParam(String tag, Map<String, Object> map) {
        logParam(tag, map, mLevel);
    }

    /**
     * 打印请求参数
     */
    public static void logParam(Map<String, Object> map, @LogLevel int level) {
        logParam(TAG, map, level);
    }

    /**
     * 打印请求参数
     */
    public static void logParam(String tag, Map<String, Object> map, @LogLevel int level) {
        if (!isEnable()) return;
        if (CollectionsUtil.isEmpty(map)) {
            emptyLog(tag, level);
            return;
        }

        line(tag, level);

        // 循环遍历
        for (Map.Entry entry : map.entrySet()) {
            String log = entry.getKey() + " : " + entry.getValue();
            writeLog(tag, log, level);
        }

        line(tag, level);
    }

    /**
     * 打印
     */
    private static void writeLog(String tag, String log, @LogLevel int level) {
        log = "[" + tag + "]" + '\t' + log;
        logger(level, log);
    }

    /**
     * 打印日志功能是否开启
     */
    private static boolean isEnable() {
        return AppConfig.IS_DEBUG;
    }

    /**
     * 数据空
     */
    private static void emptyLog(String tag, @LogLevel int level) {
        String log = "[" + tag + "]" + '\t' + "empty log!!!";
        logger(level, log);
    }

    /**
     * line, 日志开始和结束位置，以便更好的查看日志
     */
    private static void line(String tag, @LogLevel int level) {
        String log = "[" + tag + "]"
                + '\t'
                + "-----------------------------------------------------------------------------------------";
        logger(level, log);
    }

    /**
     * 根据日志级别输出日志
     */
    private static void logger(@LogLevel int level, String log) {
        switch (level) {
            default:
            case LogLevel.LEVEL_TRACE:
                mLogger.trace(log);
                break;
            case LogLevel.LEVEL_DEBUG:
                mLogger.debug(log);
                break;
            case LogLevel.LEVEL_INFO:
                mLogger.info(log);
                break;
            case LogLevel.LEVEL_WARN:
                mLogger.warn(log);
                break;
            case LogLevel.LEVEL_ERROR:
                mLogger.error(log);
                break;
        }
    }

}
