package ir;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.text.*;

/**
 * Title: Document
 * Description:  Contains all information about a document.  This is really a
 *               TREC document as the structured data contained in this document
 *               e.g.; title, dateline, etc. are TREC-specific.
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author       DG
 * @version 1.0
 *

/* Document Objects  -- stores contents of a document -- built by the */
/* parser.                                                            */

public class Document implements java.io.Serializable {

  private String  dateline;      /* date document was written                  */
  private HashMap distinctTerms; /* stores distinct terms with tf              */
  private int     documentID;    /* unique document identifier                 */
  private String  docName;       /* unique name assigned to the document       */
  private boolean endOfFile;     /* end of file flag                           */
  private String  inputFileName; /* the file that contains this document       */
  public  int     length;        /* size of the document                       */
  public  int     offset;        /* position in file of the start of the doc   */
  private String  token = "";    /* current value of the token being read      */
  private String  title;         /* title of the document                      */


  /* accept arguments which tell us how to read in the document */
  /* then call private method to do the reading                 */
  Document (int id, String d, String t, String dl, String fn, int o, int l, HashMap dt) {
      documentID = id;
      length     = l;
      title      = t;
      dateline   = dl;
      docName    = d;
      inputFileName = fn;
      offset     = o;
      distinctTerms  = dt;
  }

  /* returns the title to the caller -- the title is updated as we read each document */
  public String getTitle() {
        return title;
  }

  /* returns the title to the caller -- title is updatd as we read each document */
  public String getDocName() {
        return docName;
  }

  public String getDateLine () {
        return dateline;
  }

  public String getInputFileName () {
        return inputFileName;
  }

  public int getLength() {
        return length;
  }

  public void printTitle() {
        System.out.println("Title --> " + title);
  }

  /* for debugging we display the list of unique terms and their tf */
  public void printTerms() {
      Set termSet;   /* set of distinct terms in the document */
      String token;  /* string to hold current token          */
      TermFrequency tf;  /* holds current term frequency      */


      /* get the set of terms in this document */
      termSet = distinctTerms.keySet();

      /* now lets have an iterator for this set */
      Iterator i = termSet.iterator();

      /* loop through the set and obtain the term frequency */
      while (i.hasNext()) {
          token = (String) i.next();
          tf = (TermFrequency) distinctTerms.get(token);

          /* do not output tags  */
          if (token.charAt(0) != '<') {
              token = SimpleIRUtilities.padToken(token);
              String docString = SimpleIRUtilities.longToString(documentID);
              System.out.println(docString+" "+token+" "+tf);
          }
      }
  }

  /* get the text of a document -- the doc object has the offset    */
  /* so we need to read the input file associated with the document */
  /* and return the text                                            */

  public String getText() {

     String result = "";
     int i;
     char c = ' ';
     Reader r = null;

     try {
        /* get a file pointer */
        InputStream is = new FileInputStream(inputFileName);

        /* now make sure the input reading is buffered */
        r = new BufferedReader ( new InputStreamReader (is));

        /* skip to the start of the document */
        r.skip(offset);
     }
     catch (IOException e) {
          System.out.println("Could not find document in file --> "+inputFileName);
          System.exit(1);
     }

     /* read the document */
     for (i=0; i < length; i++) {
        try {
            c = (char) r.read();
            if (c == '<') {
                result = result + "&lt;";
            } else if (c == '>') {
                result = result + "&gt;";
            } else  {
                result = result + c;
            }

        }
        catch (IOException e) {
            System.out.println("Could not read character in file ==> " +inputFileName);
        }
     }
     return result;
  }

  /* return current document identifier */
  public int getDocumentID() {
      return documentID;
  }

  public String getStringDocumentID() {
      return SimpleIRUtilities.longToString(documentID);
  }

  /* return current term map  */
  public HashMap getTerms() {
      return distinctTerms;
  }

}
