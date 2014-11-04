package org.tharrisx.util.version;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Properties;

import org.tharrisx.util.NullHandlingUtils;
import org.tharrisx.util.dateformat.SafeDateFormat;
import org.tharrisx.util.dateformat.SafeDateFormatFactory;

/**
 * Defines all the information regarding a particular version of a "unit" or
 * software component. Also provides various lazily-cached formatted
 * presentation methods.
 * 
 * @author tharrisx
 * @since 1.0.0
 * @version 1.0.0
 */
public class Version implements Serializable {

  private static final long serialVersionUID = 7378182030887785621L;

  private static final String FILENAME = "version.properties";

  private static final String PROP_UNIT_NAME = "unitName";
  private static final String PROP_MAJOR = "major";
  private static final String PROP_MINOR = "minor";
  private static final String PROP_SUB_MINOR = "subMinor";
  private static final String PROP_SUB_SUB_MINOR = "subSubMinor";
  private static final String PROP_RELEASE = "release";
  private static final String PROP_BUILD_STAMP = "buildStamp";
  private static final String PROP_BUILD_NUMBER = "buildNumber";
  private static final String PROP_AUTHOR = "author";
  private static final String PROP_AUTHOR_EMAIL = "authorEmail";
  private static final String PROP_COPYRIGHT_YEAR = "copyrightYear";
  private static final String PROP_COPYRIGHT_HOLDER = "copyrightHolder";
  private static final String PROP_DB_MAJOR = "dbMajor";
  private static final String PROP_DB_MINOR = "dbMinor";

  private static final transient SafeDateFormat DF = SafeDateFormatFactory.get("MM/dd/yyyy hh:mm a");

  private String unitName = null;

  final void setUnitName(String arg) {
    this.unitName = arg;
  }

  public final String getUnitName() {
    return this.unitName;
  }

  private short major = 0;

  final void setMajor(short arg) {
    this.major = arg;
  }

  public final short getMajor() {
    return this.major;
  }

  private short minor = 0;

  final void setMinor(short arg) {
    this.minor = arg;
  }

  public final short getMinor() {
    return this.minor;
  }

  private short subMinor = 0;

  final void setSubMinor(short arg) {
    this.subMinor = arg;
  }

  public final short getSubMinor() {
    return this.subMinor;
  }

  private short subSubMinor = 0;

  final void setSubSubMinor(short arg) {
    this.subSubMinor = arg;
  }

  public final short getSubSubMinor() {
    return this.subSubMinor;
  }

  private Date buildStamp = null;

  final void setBuildStamp(Date arg) {
    this.buildStamp = NullHandlingUtils.nullSafeClone(arg);
  }

  public final Date getBuildStamp() {
    return NullHandlingUtils.nullSafeClone(this.buildStamp);
  }

  private String release = null;

  final void setRelease(String arg) {
    this.release = arg;
  }

  public final String getRelease() {
    return this.release;
  }

  private int buildNumber = 0;

  final void setBuildNumber(int arg) {
    this.buildNumber = arg;
  }

  public final int getBuildNumber() {
    return this.buildNumber;
  }

  private String author = null;

  final void setAuthor(String arg) {
    this.author = arg;
  }

  public final String getAuthor() {
    return this.author;
  }

  private String authorEmail = null;

  final void setAuthorEmail(String arg) {
    this.authorEmail = arg;
  }

  public final String getAuthorEmail() {
    return this.authorEmail;
  }

  private String copyrightYear = null;

  final void setCopyrightYear(String arg) {
    this.copyrightYear = arg;
  }

  public final String getCopyrightYear() {
    return this.copyrightYear;
  }

  private String copyrightHolder = null;

  final void setCopyrightHolder(String arg) {
    this.copyrightHolder = arg;
  }

  public final String getCopyrightHolder() {
    return this.copyrightHolder;
  }

  private int dbMajor = 0;

  final void setDbMajor(int arg) {
    this.dbMajor = arg;
  }

  public final int getDbMajor() {
    return this.dbMajor;
  }

  private int dbMinor = 0;

  final void setDbMinor(int arg) {
    this.dbMinor = arg;
  }

  public final int getDbMinor() {
    return this.dbMinor;
  }

  // cached aggregations

  private String fullname = null;

  public final String getFullName() {
    if(null == this.fullname) {
      this.fullname = new StringBuffer().append(getUnitName()).append(' ').append(getVersionStringWithBuildInfo()).toString();
    }
    return this.fullname;
  }

  private String versionString = null;

  public final String getVersionString() {
    if(null == this.versionString) {
      // v#.#.#.#<rel>
      this.versionString = new StringBuffer().append('v').append(getMajor()).append('.').append(getMinor()).append('.').append(getSubMinor()).append('.').append(getSubSubMinor()).toString();
      if(getRelease() != null && getRelease().trim().length() > 0)
        this.versionString += getRelease();
    }
    return this.versionString;
  }

  private String versionStringWithBuildInfo = null;

  public final String getVersionStringWithBuildInfo() {
    if(null == this.versionStringWithBuildInfo) {
      this.versionStringWithBuildInfo = new StringBuffer().append(getVersionString()).append(" (Build #").append(getBuildNumber()).append(" built on ").append(DF.format(getBuildStamp())).append(')')
          .toString();
    }
    return this.versionStringWithBuildInfo;
  }

  private String dbVersionString = null;

  public final String getDbVersionString() {
    if(null == this.dbVersionString) {
      this.dbVersionString = new StringBuffer().append('v').append(getDbMajor()).append('.').append(getDbMinor()).toString();
    }
    return this.dbVersionString;
  }

  private String authorInfo = null;

  public final String getAuthorInfo() {
    if(null == this.authorInfo) {
      this.authorInfo = new StringBuffer().append("<a href=\"mailto:").append(getAuthorEmail()).append("\">").append(getAuthor()).append("</a>").toString();
    }
    return this.authorInfo;
  }

  private String copyrightNotice = null;

  public final String getCopyrightNotice() {
    if(null == this.copyrightNotice) {
      this.copyrightNotice = new StringBuffer().append("&copy;").append(getCopyrightYear()).append(' ').append(getCopyrightHolder()).toString();
    }
    return this.copyrightNotice;
  }

  /**
   * Empty version constructor for serialization and for when an error finding
   * the version occurs, and you just want something with bean properties there,
   * in scope, to make sure that pages are shown even without version data
   * working.
   */
  public Version() {
    // ignore
  }

  /**
   * Actually construct the canonical Version object for the specified package.
   * 
   * @param packageName String The Java fully-qualified package name in which the
   *                           version.properties file requested can be found.
   * @throws VersionNotFoundException
   */
  Version(String packageName) throws VersionNotFoundException {
    init(loadProperties(packageName));
  }

  /**
   * Actually construct the canonical Version object from the specified
   * Properties object.
   * 
   * @param versionProperties Properties
   */
  public Version(Properties versionProperties) {
    init(versionProperties);
  }

  private Properties loadProperties(String packageName) throws VersionNotFoundException {
    Properties ret = new Properties();
    String versionPropFilename = null;
    // the path separator char is ALWAYS forward-slash in jars! Don't use File.pathSeparator.
    versionPropFilename = packageName.replace('.', '/') + '/' + FILENAME;
    ClassLoader cl = Version.class.getClassLoader();
    InputStream is = cl.getResourceAsStream(versionPropFilename);
    try {
      ret.load(is);
    } catch(IOException e) {
      throw new VersionNotFoundException(packageName, versionPropFilename, e);
    }
    return ret;
  }

  private void init(Properties versionProperties) {
    setUnitName(versionProperties.getProperty(PROP_UNIT_NAME));
    setMajor(Short.parseShort(versionProperties.getProperty(PROP_MAJOR)));
    setMinor(Short.parseShort(versionProperties.getProperty(PROP_MINOR)));
    setSubMinor(Short.parseShort(versionProperties.getProperty(PROP_SUB_MINOR)));
    setSubSubMinor(Short.parseShort(versionProperties.getProperty(PROP_SUB_SUB_MINOR)));
    setRelease(versionProperties.getProperty(PROP_RELEASE));
    try {
      setBuildStamp(DF.parse(versionProperties.getProperty(PROP_BUILD_STAMP)));
    } catch(ParseException e) {
      setBuildStamp(new Date(0L));
    }
    try {
      setBuildNumber(Integer.parseInt(versionProperties.getProperty(PROP_BUILD_NUMBER)));
    } catch(NumberFormatException nfe) {
      DecimalFormat df = new DecimalFormat("###,###,##0");
      setBuildNumber(df.parse(versionProperties.getProperty(PROP_BUILD_NUMBER), new ParsePosition(0)).intValue());
    }
    setAuthor(versionProperties.getProperty(PROP_AUTHOR));
    setAuthorEmail(versionProperties.getProperty(PROP_AUTHOR_EMAIL));
    setCopyrightYear(versionProperties.getProperty(PROP_COPYRIGHT_YEAR));
    setCopyrightHolder(versionProperties.getProperty(PROP_COPYRIGHT_HOLDER));
    // DB version props are optional
    try {
      setDbMajor(Integer.parseInt(versionProperties.getProperty(PROP_DB_MAJOR)));
      setDbMinor(Integer.parseInt(versionProperties.getProperty(PROP_DB_MINOR)));
    } catch(NumberFormatException e) { /* ignore */
    }
  }
}
