package org.tharrisx.util.spring;

import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.tharrisx.util.log.Log;
import org.tharrisx.util.version.VersionFactory;
import org.tharrisx.util.version.VersionNotFoundException;

/**
 * A spring bean which logs at INFO level the version info of all the unit packages in the constructor.
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
@Configurable
public class VersionStampsLogger {

  public VersionStampsLogger() {
    // nothing
  }

  /**
   * Logs at INFO level the version info of all the unit packages.
   * @param unitPackages List<String>
   */
  public VersionStampsLogger(List<String> unitPackages) {
    for(String unitPackage : unitPackages) {
      if(Log.isInfoEnabled(VersionStampsLogger.class))
        try {
          Log.info(VersionStampsLogger.class, "", VersionFactory.getVersion(unitPackage).getFullName());
        } catch(VersionNotFoundException e) {
          Log.info(VersionStampsLogger.class, "", "No Version found for unit: " + unitPackage);
        }
    }
  }
}
