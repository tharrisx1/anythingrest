package org.tharrisx.util.version;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Builds and caches Version objects for the root package names of each
 * versioned component of the platform.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class VersionFactory {

  private static final Map<String, Version> VERSIONS = new HashMap<>(8);

  private static final Lock VERSIONS_LOCK = new ReentrantLock(true);

  /**
   * Retrieves the version for the package name specified.
   * @param packageName
   * @return Version
   * @throws VersionNotFoundException
   */
  public static Version getVersion(String packageName) throws VersionNotFoundException {
    Version ret = null;
    VERSIONS_LOCK.lock();
    try {
      ret = retrieveVersion(packageName);
      if(null == ret) {
        ret = loadAndCacheVersion(packageName);
      }
    } finally {
      VERSIONS_LOCK.unlock();
    }
    return ret;
  }

  private static Version retrieveVersion(String packageName) {
    return VERSIONS.get(packageName);
  }

  private static Version loadAndCacheVersion(String packageName) throws VersionNotFoundException {
    Version ret = new Version(packageName);
    VERSIONS.put(packageName, ret);
    return ret;
  }

  private VersionFactory() {
    // private constructor for utility class
  }
}
