package org.tharrisx.util.reflect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.tharrisx.util.log.Log;

/**
 * Retrieves class references by looking in the classpath you provide
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class PackageSearch {

  @SuppressWarnings("unchecked")
  public static <T> List<Class<? extends T>> getMatchingClasses(List<String> packageNames, Class<T> assignableTo) {
    if(Log.isEnteringEnabled(PackageSearch.class))
      Log.entering(PackageSearch.class, "getMatchingClasses", packageNames, assignableTo);
    List<Class<? extends T>> ret = null;
    try {
      List<Class<? extends T>> aggregator = new LinkedList<>();
      Class<T> matchingClass = null;
      for(String packageName : packageNames) {
        if(null != packageName) {
          for(Class<?> unknownClazz : getClasses(packageName)) {
            if(null != unknownClazz && assignableTo.isAssignableFrom(unknownClazz)) {
              if(Log.isDebugEnabled(PackageSearch.class))
                Log.debug(PackageSearch.class, "getMatchingClasses", "Found matching class '" + matchingClass + "'");
              matchingClass = (Class<T>) unknownClazz;
              aggregator.add(matchingClass);
            }
          }
        }
      }
      ret = Collections.unmodifiableList(aggregator);
      return ret;
    } catch(IOException e) {
      Log.error(PackageSearch.class, "getMatchingClasses", e.getMessage(), e);
      return ret;
    } catch(ClassNotFoundException e) {
      Log.error(PackageSearch.class, "getMatchingClasses", e.getMessage(), e);
      return ret;
    } finally {
      if(Log.isExitingEnabled(PackageSearch.class))
        Log.exiting(PackageSearch.class, "getMatchingClasses", ret);
    }
  }

  private static Set<Class<?>> getClasses(String packageName) throws IOException, ClassNotFoundException {
    if(Log.isEnteringEnabled(PackageSearch.class))
      Log.entering(PackageSearch.class, "getClasses", packageName);
    Set<Class<?>> ret = null;
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      ret = getClassesFromLoader(loader, packageName);
      return ret;
    } finally {
      if(Log.isExitingEnabled(PackageSearch.class))
        Log.exiting(PackageSearch.class, "getClasses", ret);
    }
  }

  private static Set<Class<?>> getClassesFromLoader(ClassLoader loader, String packageName) throws IOException, ClassNotFoundException {
    if(Log.isEnteringEnabled(PackageSearch.class))
      Log.entering(PackageSearch.class, "getClassesFromLoader", loader, packageName);
    Set<Class<?>> ret = new HashSet<>();
    try {
      String path = packageName.replace('.', '/');
      Enumeration<URL> resources = loader.getResources(path);
      if(resources != null) {
        while(resources.hasMoreElements()) {
          String filePath = resources.nextElement().getFile();
          // WINDOWS HACK
          if(filePath.indexOf("%20") > 0)
            filePath = filePath.replaceAll("%20", " ");
          if(filePath != null) {
            if((filePath.indexOf("!") > 0) && (filePath.indexOf(".jar") > 0)) {
              String jarPath = filePath.substring(0, filePath.indexOf("!")).substring(filePath.indexOf(":") + 1);
              // WINDOWS HACK
              if(jarPath.indexOf(":") >= 0)
                jarPath = jarPath.substring(1);
              ret.addAll(getFromJARFile(jarPath, path));
            } else {
              ret.addAll(getFromDirectory(new File(filePath), packageName));
            }
          }
        }
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(PackageSearch.class))
        Log.exiting(PackageSearch.class, "getClassesFromLoader", ret);
    }
  }

  private static Set<Class<?>> getFromDirectory(File directory, String packageName) throws ClassNotFoundException {
    if(Log.isEnteringEnabled(PackageSearch.class))
      Log.entering(PackageSearch.class, "getFromDirectory", directory, packageName);
    Set<Class<?>> ret = new HashSet<>();
    try {
      if(directory.exists()) {
        String[] filenames = directory.list();
        for(int cv = 0; cv < filenames.length; cv++) {
          if(filenames[cv].endsWith(".class")) {
            String name = packageName + '.' + stripFilenameExtension(filenames[cv]);
            Class<?> clazz = Class.forName(name);
            ret.add(clazz);
          }
        }
      }
      return ret;
    } finally {
      if(Log.isExitingEnabled(PackageSearch.class))
        Log.exiting(PackageSearch.class, "getClassesFromLoader", ret);
    }
  }

  private static Set<Class<?>> getFromJARFile(String jar, String packageName) throws ClassNotFoundException, FileNotFoundException, IOException {
    if(Log.isEnteringEnabled(PackageSearch.class))
      Log.entering(PackageSearch.class, "getFromJARFile", jar, packageName);
    Set<Class<?>> ret = new HashSet<>();
    JarInputStream jarFile = null;
    try {
      jarFile = new JarInputStream(new FileInputStream(jar));
      JarEntry jarEntry;
      do {
        jarEntry = jarFile.getNextJarEntry();
        if(jarEntry != null) {
          String className = jarEntry.getName();
          if(className.endsWith(".class")) {
            className = stripFilenameExtension(className);
            if(className.startsWith(packageName))
              ret.add(Class.forName(className.replace('/', '.')));
          }
        }
      } while(jarEntry != null);
      return ret;
    } finally {
      if(null != jarFile) {
        jarFile.close();
      }
      if(Log.isExitingEnabled(PackageSearch.class))
        Log.exiting(PackageSearch.class, "getFromJARFile", ret);
    }
  }

  private static String stripFilenameExtension(String arg) {
    //if(Log.isEnteringEnabled(PackageSearch.class)) Log.entering(PackageSearch.class, "stripFilenameExtension", arg);
    String ret = null;
    try {
      ret = arg.substring(0, arg.lastIndexOf('.'));
      return ret;
    } finally {
      //if(Log.isExitingEnabled(PackageSearch.class)) Log.exiting(PackageSearch.class, "stripFilenameExtension", ret);
    }
  }
}
