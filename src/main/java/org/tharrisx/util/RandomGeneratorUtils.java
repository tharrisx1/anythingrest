package org.tharrisx.util;

import java.util.Random;

/**
 * Provides utility methods for randomized primitives.
 * @author tharrisx
 * @version 1.0.0
 * @since 1.0.0
 */
public class RandomGeneratorUtils {

  private static final Random R = new Random();

  public static int nextInt(int bound) {
    return R.nextInt(bound);
  }

  public static boolean randomBoolean() {
    return R.nextBoolean();
  }

  public static int randomInt(int low, int high) {
    return nextInt(high - low) + low;
  }

  public static char randomDigit() {
    return "0123456789".charAt(randomInt(0, 10));
  }

  public static char randomLetter() {
    return "abcdefghijklmnopqrstuvwxyz".charAt(randomInt(0, 26));
  }

  public static String randomString(int lowNumChars, int highNumChars) {
    StringBuilder retBuf = new StringBuilder();
    int numChars = randomInt(lowNumChars, highNumChars);
    for(int cv = 0; cv < numChars; cv++)
      retBuf.append(randomLetter());
    return retBuf.toString();
  }

  public static String randomEmail() {
    return new StringBuilder(randomString(4, 10)).append('@').append(randomString(4, 10)).append(".com").toString();
  }

  public static String randomCountry() {
    return COUNTRIES[randomInt(0, COUNTRIES.length)][1];
  }

  public static String randomState() {
    return STATES[randomInt(0, STATES.length)][1];
  }

  public static String randomZipcode() {
    StringBuilder retBuf = new StringBuilder();
    for(int cv = 0; cv < 5; cv++)
      retBuf.append(randomDigit());
    return retBuf.toString();
  }

  private static final String[][] COUNTRIES = { { "AFGHANISTAN", "AF" }, { "LAND ISLANDS", "AX" }, { "ALBANIA", "AL" }, { "ALGERIA", "DZ" }, { "AMERICAN SAMOA", "AS" }, { "ANDORRA", "AD" },
      { "ANGOLA", "AO" }, { "ANGUILLA", "AI" }, { "ANTARCTICA", "AQ" }, { "ANTIGUA AND BARBUDA", "AG" }, { "ARGENTINA", "AR" }, { "ARMENIA", "AM" }, { "ARUBA", "AW" }, { "AUSTRALIA", "AU" },
      { "AUSTRIA", "AT" }, { "AZERBAIJAN", "AZ" }, { "BAHAMAS", "BS" }, { "BAHRAIN", "BH" }, { "BANGLADESH", "BD" }, { "BARBADOS", "BB" }, { "BELARUS", "BY" }, { "BELGIUM", "BE" },
      { "BELIZE", "BZ" }, { "BENIN", "BJ" }, { "BERMUDA", "BM" }, { "BHUTAN", "BT" }, { "BOLIVIA, PLURINATIONAL STATE OF", "BO" }, { "BOSNIA AND HERZEGOVINA", "BA" }, { "BOTSWANA", "BW" },
      { "BOUVET ISLAND", "BV" }, { "BRAZIL", "BR" }, { "BRITISH INDIAN OCEAN TERRITORY", "IO" }, { "BRUNEI DARUSSALAM", "BN" }, { "BULGARIA", "BG" }, { "BURKINA FASO", "BF" }, { "BURUNDI", "BI" },
      { "CAMBODIA", "KH" }, { "CAMEROON", "CM" }, { "CANADA", "CA" }, { "CAPE VERDE", "CV" }, { "CAYMAN ISLANDS", "KY" }, { "CENTRAL AFRICAN REPUBLIC", "CF" }, { "CHAD", "TD" }, { "CHILE", "CL" },
      { "CHINA", "CN" }, { "CHRISTMAS ISLAND", "CX" }, { "COCOS (KEELING) ISLANDS", "CC" }, { "COLOMBIA", "CO" }, { "COMOROS", "KM" }, { "CONGO", "CG" },
      { "CONGO, THE DEMOCRATIC REPUBLIC OF THE", "CD" }, { "COOK ISLANDS", "CK" }, { "COSTA RICA", "CR" }, { "COTE D'IVOIRE", "CI" }, { "CROATIA", "HR" }, { "CUBA", "CU" }, { "CYPRUS", "CY" },
      { "CZECH REPUBLIC", "CZ" }, { "DENMARK", "DK" }, { "DJIBOUTI", "DJ" }, { "DOMINICA", "DM" }, { "DOMINICAN REPUBLIC", "DO" }, { "ECUADOR", "EC" }, { "EGYPT", "EG" }, { "EL SALVADOR", "SV" },
      { "EQUATORIAL GUINEA", "GQ" }, { "ERITREA", "ER" }, { "ESTONIA", "EE" }, { "ETHIOPIA", "ET" }, { "FALKLAND ISLANDS (MALVINAS)", "FK" }, { "FAROE ISLANDS", "FO" }, { "FIJI", "FJ" },
      { "FINLAND", "FI" }, { "FRANCE", "FR" }, { "FRENCH GUIANA", "GF" }, { "FRENCH POLYNESIA", "PF" }, { "FRENCH SOUTHERN TERRITORIES", "TF" }, { "GABON", "GA" }, { "GAMBIA", "GM" },
      { "GEORGIA", "GE" }, { "GERMANY", "DE" }, { "GHANA", "GH" }, { "GIBRALTAR", "GI" }, { "GREECE", "GR" }, { "GREENLAND", "GL" }, { "GRENADA", "GD" }, { "GUADELOUPE", "GP" }, { "GUAM", "GU" },
      { "GUATEMALA", "GT" }, { "GUERNSEY", "GG" }, { "GUINEA", "GN" }, { "GUINEA-BISSAU", "GW" }, { "GUYANA", "GY" }, { "HAITI", "HT" }, { "HEARD ISLAND AND MCDONALD ISLANDS", "HM" },
      { "HOLY SEE (VATICAN CITY STATE)", "VA" }, { "HONDURAS", "HN" }, { "HONG KONG", "HK" }, { "HUNGARY", "HU" }, { "ICELAND", "IS" }, { "INDIA", "IN" }, { "INDONESIA", "ID" },
      { "IRAN, ISLAMIC REPUBLIC OF", "IR" }, { "IRAQ", "IQ" }, { "IRELAND", "IE" }, { "ISLE OF MAN", "IM" }, { "ISRAEL", "IL" }, { "ITALY", "IT" }, { "JAMAICA", "JM" }, { "JAPAN", "JP" },
      { "JERSEY", "JE" }, { "JORDAN", "JO" }, { "KAZAKHSTAN", "KZ" }, { "KENYA", "KE" }, { "KIRIBATI", "KI" }, { "KOREA, DEMOCRATIC PEOPLE'S REPUBLIC OF", "KP" }, { "KOREA, REPUBLIC OF", "KR" },
      { "KUWAIT", "KW" }, { "KYRGYZSTAN", "KG" }, { "LAO PEOPLE'S DEMOCRATIC REPUBLIC", "LA" }, { "LATVIA", "LV" }, { "LEBANON", "LB" }, { "LESOTHO", "LS" }, { "LIBERIA", "LR" },
      { "LIBYAN ARAB JAMAHIRIYA", "LY" }, { "LIECHTENSTEIN", "LI" }, { "LITHUANIA", "LT" }, { "LUXEMBOURG", "LU" }, { "MACAO", "MO" }, { "MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF", "MK" },
      { "MADAGASCAR", "MG" }, { "MALAWI", "MW" }, { "MALAYSIA", "MY" }, { "MALDIVES", "MV" }, { "MALI", "ML" }, { "MALTA", "MT" }, { "MARSHALL ISLANDS", "MH" }, { "MARTINIQUE", "MQ" },
      { "MAURITANIA", "MR" }, { "MAURITIUS", "MU" }, { "MAYOTTE", "YT" }, { "MEXICO", "MX" }, { "MICRONESIA, FEDERATED STATES OF", "FM" }, { "MOLDOVA, REPUBLIC OF", "MD" }, { "MONACO", "MC" },
      { "MONGOLIA", "MN" }, { "MONTENEGRO", "ME" }, { "MONTSERRAT", "MS" }, { "MOROCCO", "MA" }, { "MOZAMBIQUE", "MZ" }, { "MYANMAR", "MM" }, { "NAMIBIA", "NA" }, { "NAURU", "NR" },
      { "NEPAL", "NP" }, { "NETHERLANDS", "NL" }, { "NETHERLANDS ANTILLES", "AN" }, { "NEW CALEDONIA", "NC" }, { "NEW ZEALAND", "NZ" }, { "NICARAGUA", "NI" }, { "NIGER", "NE" }, { "NIGERIA", "NG" },
      { "NIUE", "NU" }, { "NORFOLK ISLAND", "NF" }, { "NORTHERN MARIANA ISLANDS", "MP" }, { "NORWAY", "NO" }, { "OMAN", "OM" }, { "PAKISTAN", "PK" }, { "PALAU", "PW" },
      { "PALESTINIAN TERRITORY, OCCUPIED", "PS" }, { "PANAMA", "PA" }, { "PAPUA NEW GUINEA", "PG" }, { "PARAGUAY", "PY" }, { "PERU", "PE" }, { "PHILIPPINES", "PH" }, { "PITCAIRN", "PN" },
      { "POLAND", "PL" }, { "PORTUGAL", "PT" }, { "PUERTO RICO", "PR" }, { "QATAR", "QA" }, { "REUNION", "RE" }, { "ROMANIA", "RO" }, { "RUSSIAN FEDERATION", "RU" }, { "RWANDA", "RW" },
      { "SAINT BARTH LEMY", "BL" }, { "SAINT HELENA", "SH" }, { "SAINT KITTS AND NEVIS", "KN" }, { "SAINT LUCIA", "LC" }, { "SAINT MARTIN", "MF" }, { "SAINT PIERRE AND MIQUELON", "PM" },
      { "SAINT VINCENT AND THE GRENADINES", "VC" }, { "SAMOA", "WS" }, { "SAN MARINO", "SM" }, { "SAO TOME AND PRINCIPE", "ST" }, { "SAUDI ARABIA", "SA" }, { "SENEGAL", "SN" }, { "SERBIA", "RS" },
      { "SEYCHELLES", "SC" }, { "SIERRA LEONE", "SL" }, { "SINGAPORE", "SG" }, { "SLOVAKIA", "SK" }, { "SLOVENIA", "SI" }, { "SOLOMON ISLANDS", "SB" }, { "SOMALIA", "SO" }, { "SOUTH AFRICA", "ZA" },
      { "SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS", "GS" }, { "SPAIN", "ES" }, { "SRI LANKA", "LK" }, { "SUDAN", "SD" }, { "SURINAME", "SR" }, { "SVALBARD AND JAN MAYEN", "SJ" },
      { "SWAZILAND", "SZ" }, { "SWEDEN", "SE" }, { "SWITZERLAND", "CH" }, { "SYRIAN ARAB REPUBLIC", "SY" }, { "TAIWAN, PROVINCE OF CHINA", "TW" }, { "TAJIKISTAN", "TJ" },
      { "TANZANIA, UNITED REPUBLIC OF", "TZ" }, { "THAILAND", "TH" }, { "TIMOR-LESTE", "TL" }, { "TOGO", "TG" }, { "TOKELAU", "TK" }, { "TONGA", "TO" }, { "TRINIDAD AND TOBAGO", "TT" },
      { "TUNISIA", "TN" }, { "TURKEY", "TR" }, { "TURKMENISTAN", "TM" }, { "TURKS AND CAICOS ISLANDS", "TC" }, { "TUVALU", "TV" }, { "UGANDA", "UG" }, { "UKRAINE", "UA" },
      { "UNITED ARAB EMIRATES", "AE" }, { "UNITED KINGDOM", "GB" }, { "UNITED STATES", "US" }, { "UNITED STATES MINOR OUTLYING ISLANDS", "UM" }, { "URUGUAY", "UY" }, { "UZBEKISTAN", "UZ" },
      { "VANUATU", "VU" }, { "VENEZUELA, BOLIVARIAN REPUBLIC OF", "VE" }, { "VIET NAM", "VN" }, { "VIRGIN ISLANDS, BRITISH", "VG" }, { "VIRGIN ISLANDS, U.S.", "VI" }, { "WALLIS AND FUTUNA", "WF" },
      { "WESTERN SAHARA", "EH" }, { "YEMEN", "YE" }, { "ZAMBIA", "ZM" }, { "ZIMBABWE", "ZW" } };

  private static final String[][] STATES = { { "ALASKA", "AK" }, { "ALABAMA", "AL" }, { "ARKANSAS", "AR" }, { "ARIZONA", "AZ" }, { "CALIFORNIA", "CA" }, { "COLORADO", "CO" }, { "CONNETICUT", "CT" },
      { "DISTRICT OF COLUMBIA", "DC" }, { "DELAWARE", "DE" }, { "FLORIDA", "FL" }, { "GEORGIA", "GA" }, { "HAWAII", "HI" }, { "IOWA", "IA" }, { "IDAHO", "ID" }, { "ILLINOIS", "IL" },
      { "INDIANA", "IN" }, { "KANSAS", "KS" }, { "KENTUCKEY", "KY" }, { "LOUISIANA", "LA" }, { "MASSACHUSETTS", "MA" }, { "MARYLAND", "MD" }, { "MAINE", "ME" }, { "MICHIGAN", "MI" },
      { "MINNESOTA", "MN" }, { "MONTANA", "MO" }, { "MISSOURI", "MS" }, { "MONTANA", "MT" }, { "NORTH CAROLINA", "NC" }, { "NORTH DAKOTA", "ND" }, { "NEBRASKA", "NE" }, { "NEW HAMPSHIRE", "NH" },
      { "NEW JERSEY", "NJ" }, { "NEW MEXICO", "NM" }, { "NEVADA", "NV" }, { "NEW YORK", "NY" }, { "OHIO", "OH" }, { "OKLAHOMA", "OK" }, { "OREGON", "OR" }, { "PENNSYLVANIA", "PA" },
      { "RHODE ISLAND", "RI" }, { "SOUTH CAROLINA", "SC" }, { "SOUTH DAKOTA", "SD" }, { "TENNESSEE", "TN" }, { "TEXAS", "TX" }, { "UTAH", "UT" }, { "VIRGINIA", "VA" }, { "VERMONT", "VT" },
      { "WASHINGTON", "WA" }, { "WISCONSIN", "WI" }, { "WEST VIRGINIA", "WV" }, { "WYOMING", "WY" } };
}
