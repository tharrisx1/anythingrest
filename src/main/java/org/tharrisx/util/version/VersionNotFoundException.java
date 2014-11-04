package org.tharrisx.util.version;

/**
 * Thrown when the package version specified in the version factory request is
 * not found, ie. the version.properties file is not found in the package's
 * folder in any of the jars in the classpath.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class VersionNotFoundException extends Exception {

  private static final long serialVersionUID = 1112398789759110201L;

  public VersionNotFoundException(String packageName, String versionPropFilename, Throwable cause) {
    super("The version.properties file was not found for package '" + packageName + "' (as file '" + versionPropFilename + "').", cause);
  }
}
