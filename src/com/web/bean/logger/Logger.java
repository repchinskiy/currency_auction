package com.web.bean.logger;

import android.util.Log;
import com.web.bean.*;


public class Logger {

    public static final String TAG = "##### CURRENCY_AUCTION";
//    public static String versionName;
//    public static String versionInfo;
//    private static String lastMessage = null;
//    private static String lastStack = null;

    public static void trace(String str) {
        Log.i(TAG, str);
    }

    public static void trace(String str, Throwable ex) {
        Log.i(TAG, str, ex);
    }

    public static void error(Throwable ex) {
        error("Error", ex);
    }

    public static void error(String str, Throwable ex) {
        error(str, ex, true);
    }

    public static void warn(String str, Throwable ex) {
        log(str, ex, true, LevelType.WARNING);
    }

    public static void error(String str, Throwable ex, boolean sendLogToServer) {
        log(str, ex, sendLogToServer, LevelType.ERROR);
    }

    private static void log(String str, Throwable ex, boolean sendLogToServer, LevelType levelType) {
        Log.e(TAG, str, ex);

//        if (sendLogToServer) {
//            ErrorLog errorLog = new ErrorLog();
//            errorLog.setAndroid("" + Build.VERSION.RELEASE);
//            errorLog.setPhone("" + Build.BRAND + " | " + Build.DEVICE + " | " + Build.MODEL);
//            errorLog.setVersionInfo(versionInfo);
//            errorLog.setMessage(str);
//            errorLog.setLevelType(levelType);
//
//
//            StringBuilder stringBuilder = new StringBuilder();
//
//            Throwable cause = ex;
//            do {
//                stringBuilder.append(cause).append("\n");
//
//                for (StackTraceElement stackTraceElement : cause.getStackTrace()) {
//                    stringBuilder.append(stackTraceElement.toString()).append("\n");
//                }
//
//                cause = cause.getCause();
//
//                if (cause != null) {
//                    stringBuilder.append("Caused by:\n");
//                }
//
//            } while (cause != null);
//
//            errorLog.setStack(stringBuilder.toString());
//
//            if ((lastMessage == null || !lastMessage.equals(errorLog.getMessage())) && (lastStack == null || !lastStack.equals(errorLog.getStack()))) {
//                lastMessage = errorLog.getMessage();
//                lastStack = errorLog.getStack();
//                WebClientSingleton.getInstance().request(new WebRequest<>(WebClient.ERROR_LOG_SERVICE, errorLog, null));
//            }
//        }
    }

    public static void log(String str, LevelType levelType) {
        Log.i(TAG, str);

//        ErrorLog errorLog = new ErrorLog();
//        errorLog.setAndroid("" + Build.VERSION.RELEASE);
//        errorLog.setPhone("" + Build.BRAND + " | " + Build.DEVICE + " | " + Build.MODEL);
//        errorLog.setVersionInfo(versionInfo);
//        errorLog.setMessage(str);
//        errorLog.setStack("");
//        errorLog.setLevelType(levelType);
//
//        WebClientSingleton.getInstance().request(new WebRequest<>(WebClient.ERROR_LOG_SERVICE, errorLog, null));
    }

    @Deprecated
    public static void info(String str, LevelType levelType) {
        log(str, levelType);
    }

    public static void info(String str) {
        log(str, LevelType.INFO);
    }

    public static void error(String msg, String str) {
//        if (msg == null) {
//            msg = "";
//        }
//        if (str == null) {
//            str = "";
//        }
//
//        str = str.replace('<', '[');
//        str = str.replace('>', ']');
//        Log.e(TAG, str);
//        ErrorLog errorLog = new ErrorLog();
//        errorLog.setAndroid("" + Build.VERSION.RELEASE);
//        errorLog.setPhone("" + Build.BRAND + " | " + Build.DEVICE + " | " + Build.MODEL);
//        errorLog.setMessage(msg);
//        errorLog.setStack(str);
//        errorLog.setVersionInfo(versionInfo);
//        errorLog.setLevelType(LevelType.ERROR);
//
//        if ((lastMessage == null || !lastMessage.equals(errorLog.getMessage())) && (lastStack == null || !lastStack.equals(errorLog.getStack()))) {
//            lastMessage = errorLog.getMessage();
//            lastStack = errorLog.getStack();
//            WebClientSingleton.getInstance().request(new WebRequest(WebClient.ERROR_LOG_SERVICE, errorLog, null));
//        }
    }
}
