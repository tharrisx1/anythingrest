package org.tharrisx.util.log;

import java.util.Arrays;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.tharrisx.util.text.StringUtils;

/**
 * A logging utility class. Wonderous!
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class Log {

  public static final String METHOD_NAME_STATIC_INITIALIZER = "static-initializer";
  public static final String METHOD_NAME_CONSTRUCTOR = "constructor";

  public static boolean isFatalEnabled(Class<?> callingClass) {
    return isEnabled(callingClass, Level.FATAL);
  }

  public static boolean isErrorEnabled(Class<?> callingClass) {
    return isEnabled(callingClass, Level.ERROR);
  }

  public static boolean isWarnEnabled(Class<?> callingClass) {
    return isEnabled(callingClass, Level.WARN);
  }

  public static boolean isInfoEnabled(Class<?> callingClass) {
    return isEnabled(callingClass, Level.INFO);
  }

  public static boolean isDebugEnabled(Class<?> callingClass) {
    return isEnabled(callingClass, Level.DEBUG);
  }

  public static boolean isTraceEnabled(Class<?> callingClass) {
    return isEnabled(callingClass, Level.TRACE);
  }

  public static boolean isEnteringEnabled(Class<?> callingClass) {
    return isTraceEnabled(callingClass);
  }

  public static boolean isExitingEnabled(Class<?> callingClass) {
    return isTraceEnabled(callingClass);
  }

  public static void fatal(Class<?> callingClass, String methodName, Object message) {
    log(callingClass, methodName, Level.FATAL, message);
  }

  public static void fatal(Class<?> callingClass, String methodName, Object message, Throwable t) {
    log(callingClass, methodName, Level.FATAL, message, t);
  }

  public static void error(Class<?> callingClass, String methodName, Object message) {
    log(callingClass, methodName, Level.ERROR, message);
  }

  public static void error(Class<?> callingClass, String methodName, Object message, Throwable t) {
    log(callingClass, methodName, Level.ERROR, message, t);
  }

  public static void warn(Class<?> callingClass, String methodName, Object message) {
    log(callingClass, methodName, Level.WARN, message);
  }

  public static void warn(Class<?> callingClass, String methodName, Object message, Throwable t) {
    log(callingClass, methodName, Level.WARN, message, t);
  }

  public static void info(Class<?> callingClass, String methodName, Object message) {
    log(callingClass, methodName, Level.INFO, message);
  }

  public static void info(Class<?> callingClass, String methodName, Object message, Throwable t) {
    log(callingClass, methodName, Level.INFO, message, t);
  }

  public static void debug(Class<?> callingClass, String methodName, Object message) {
    log(callingClass, methodName, Level.DEBUG, message);
  }

  public static void debug(Class<?> callingClass, String methodName, Object message, Throwable t) {
    log(callingClass, methodName, Level.DEBUG, message, t);
  }

  public static void trace(Class<?> callingClass, String methodName, Object message) {
    log(callingClass, methodName, Level.TRACE, message);
  }

  public static void trace(Class<?> callingClass, String methodName, Object message, Throwable t) {
    log(callingClass, methodName, Level.TRACE, message, t);
  }

  public static void entering(Class<?> callingClass, String methodName, Object... args) {
    trace(callingClass, methodName, MSG_ENTERING + makeArgumentList(args));
  }

  public static void exiting(Class<?> callingClass, String methodName) {
    trace(callingClass, methodName, MSG_EXITING + ARG_LIST_FRONT + ARG_LIST_BACK);
  }

  public static void exiting(Class<?> callingClass, String methodName, Object returnVal) {
    trace(callingClass, methodName, MSG_EXITING + handleArgument(returnVal));
  }

  // internal constants
  private static final String MSG_ENTERING = "> enter > ";
  private static final String MSG_EXITING = "<  exit < ";
  private static final String ARG_LIST_FRONT = "{";
  private static final String ARG_LIST_BACK = "}";
  private static final String ARY_LIST_FRONT = "[";
  private static final String ARY_LIST_BACK = "]";
  private static final String ARG_LIST_DELIM = ", ";

  // internal implementation methods

  private static void log(Class<?> callingClass, String methodName, Level level, Object message) {
    log(callingClass, methodName, level, message, null);
  }

  private static void log(Class<?> callingClass, String methodName, Level level, Object message, Throwable t) {
    Logger relevantLogger = Logger.getLogger(callingClass);
    if(isEnabled(relevantLogger, level)) {
      if(null == t) {
        relevantLogger.log(level, getStackFrameInfo(callingClass, methodName) + message);
      } else {
        relevantLogger.log(level, getStackFrameInfo(callingClass, methodName) + message, t);
      }
    }
  }

  private static boolean isEnabled(Logger relevantLogger, Level level) {
    return relevantLogger.isEnabledFor(level);
  }

  private static boolean isEnabled(Class<?> callingClass, Level level) {
    return Logger.getLogger(callingClass).isEnabledFor(level);
  }

  private static String makeArgumentList(Object... args) {
    StringBuilder retBuf = new StringBuilder(ARG_LIST_FRONT);

    // for(Object arg : args) {
    Object arg = null;
    for(int cv = 0; cv < args.length; cv++) {
      arg = args[cv];
      if(!ARG_LIST_FRONT.equals(retBuf.toString()))
        retBuf.append(ARG_LIST_DELIM);
      retBuf.append(handleArgument(arg));
    }
    retBuf.append(ARG_LIST_BACK);
    return retBuf.toString();
  }

  private static String handleArgument(Object arg) {
    String ret = null;
    if(null == arg) {
      ret = "(null)";
    } else if(arg.getClass().equals(Object[].class) && ((Object[]) arg).length == 0) {
      // don't try and log the empty array!! That would be dumb.
      ret = arg.toString();
    } else if(arg.getClass().equals(byte[].class)) {
      // don't try and log the byte array!! That would be dumb.
      ret = arg.toString();
    } else if(arg.getClass().isArray()) {
      StringBuilder retBuf = new StringBuilder(ARY_LIST_FRONT);
      for(Object arrayItem : Arrays.asList((Object[]) arg)) {
        if(!ARY_LIST_FRONT.equals(retBuf.toString()))
          retBuf.append(ARG_LIST_DELIM);
        retBuf.append(handleArgument(arrayItem));
      }
      retBuf.append(ARY_LIST_BACK);
      ret = retBuf.toString();
    } else {
      ret = arg.toString();
    }
    return ret;
  }

  private static String getStackFrameInfo(Class<?> loggingClass, String methodName) {
    // format "(class.method) "
    String ret = null;
    StringBuilder buf = new StringBuilder();
    buf.append('(').append(StringUtils.stripPackage(loggingClass.getName())).append('.').append(methodName).append(") ");
    ret = buf.toString();
    return ret;
  }
}
