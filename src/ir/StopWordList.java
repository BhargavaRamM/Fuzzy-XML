package ir;
import java.io.*;
import java.util.*;

/**
 * Title:  StopWordList
 * Description:  Handles all stop word activity
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author  DG
 * @version 1.0
 */

 /* potential exercises  -- extend to be a subclass of hashset */
 /* change to an array with a binary search                    */


public class StopWordList {

  StreamTokenizer in; /* actual file handle pointer used to read stop list  */
  HashSet stopWordSet;


  /* constructor reads in a stop word list */
  public StopWordList (String stopWordFileName) {
     try {
            /* get a file pointer */
            InputStream is = new FileInputStream(stopWordFileName);

            /* now make sure the input reading is buffered */
            Reader r = new BufferedReader ( new InputStreamReader (is));

            /* now set up the stream tokenizer which will parse this */
            in = new StreamTokenizer (r);
            in.wordChars('\'','\'');  /* don't skip apostrophe (') */
         }
	catch (FileNotFoundException e) {
            System.out.println("Stop word file not found");
            return;
      }

      /* calls an internal method to read the stop word list */
      stopWordSet = new HashSet();
      read();
 }

  /* internal method to read the stop word list */
  private void read() {
      String token = "";

      try {
          while (in.nextToken() != in.TT_EOF) {
            token = in.sval;
            stopWordSet.add(token);
          }
      } catch ( IOException e ) {
        System.out.println("Finished reading stop words");
      }
  }

  /* find out if the word appears in the stop word list */
  public boolean contains(String s) {
      return (stopWordSet.contains(s));
  }
}

