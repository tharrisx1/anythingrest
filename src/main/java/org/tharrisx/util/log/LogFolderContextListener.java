package org.tharrisx.util.log;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This class can be configured in Tomcat to listen for the context being initialized, and take the JNDI String
 * value named "logFolder" and place it into the System properties so that our Log4J configuration files can use
 * it as a variable, as "${logFolder}".
 * 
 * @author $LastChangedBy: tharris $
 * @version $LastChangedRevision: 6240 $ $LastChangedDate: 2006-12-26 00:17:03 -0500 (Tue, 26 Dec 2006) $
 * @since 1.0.0
 */
public class LogFolderContextListener implements ServletContextListener {

  /**
   * @param sce ServletContextEvent
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      String logFolder = (String) ((Context) (new InitialContext().lookup("java:comp/env"))).lookup("logFolder");
      System.setProperty("logFolder", logFolder);
    } catch(NamingException e) {
      System.out.println("ERROR! Can't get logFolder setting from JNDI to configure webapps' log folders.");
      e.printStackTrace(System.out);
    }
  }

  /**
   * @param sce ServletContextEvent
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // nothing
  }
}
