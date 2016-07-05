package ir;
import java.util.Date;



/**
 * Title:        New IR System
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author
 * @version 1.0
 */

public class SimpleIRUtilities   {

         /* return time to build or load the index */
  public static String getElapsedTime(Date startDate, Date endDate) {

    long microseconds;
    long seconds;
    long minutes;
    long hours;
    String result;

    result = new String();
    long j = 0;
    microseconds = endDate.getTime() - startDate.getTime();
    seconds = microseconds / 1000;
    hours = seconds / 3600;
    seconds = seconds - (hours * 3600);
    minutes = seconds / 60;
    seconds = seconds - (minutes * 60);
    microseconds = microseconds - (((hours * 3600) + (minutes * 60) + seconds) * 1000);
    result = hours + ":" + minutes + ":" + seconds + "." + microseconds;
    return result;
  }

  /* pad document id with blanks if necessary     */
  /* this is just for debugging purposes          */
  public static String longToString (long l) {
    if (l  > 999999999 ) return ""+l;
    if (l  > 99999999 )  return " "+l;
    if (l  > 9999999 )   return "  "+l;
    if (l  > 999999 )    return "   "+l;
    if (l  > 99999 )     return "    "+l;
    if (l  > 9999 )      return "     "+l;
    if (l  > 999 )       return "      "+l;
    if (l  > 99 )        return "       "+l;
    if (l  > 9 )         return "        "+l;
    return "         "+l;
  }

  /* pad token with blanks if necessary, just for debugging output  */
  public static String padToken(String token) {

    int MAX_TOKEN_SIZE = 15;
    StringBuffer s = new StringBuffer(token);

    /* pad token with blanks if necessary */
    int len = token.length();
    if (len < 15) {
        for (int j=token.length(); j < MAX_TOKEN_SIZE; j++) {
             s.append(" ");
        }
    } else {
        /* do a set length to truncate long tokens */
        s.setLength(MAX_TOKEN_SIZE);
    }
    return s.toString();
  }

}
