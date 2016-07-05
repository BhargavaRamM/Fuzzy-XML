package ir;
import java.lang.*;
import java.io.*;

/**
 * Title:        New IR System
 * Description:  This will store a term frequency -- useful for hash tables,
 *               tf is stored as a short.
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author
 * @version 1.0
 */

/* simple wrapper for the term frequency                     */
/* this makes it so our code is immune to any change in the  */
/* datatype of the term frequency                            */
/* also hides truncation from the user                       */

public class TermFrequency implements Serializable {

  private static final short MAX_VALUE = 32767;
  short tf;

  public TermFrequency() {
      tf = 1;
  }

  /* increment the tf as long as we have room for an increment */
  public void Increment () {
      if (tf <= MAX_VALUE) {
          tf = (short) (tf + 1);
      }
  }

  public void Set (short value) {
      tf = value;
  }

  public short getTF () {
      return tf;
  }

  public String toString() {
      if (tf > 9999) return ""+tf;
      if (tf > 999)  return " "+tf;
      if (tf > 99)   return "  "+tf;
      if (tf > 9)    return "    "+tf;
      return "     "+tf;
  }
}

