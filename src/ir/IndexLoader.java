package ir;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;
import ir.SimpleStreamTokenizer;

/* this class loads the inverted index */
public class IndexLoader  implements java.io.Serializable {

  private Properties configSettings;     /* config settings object                 */
  private Date       startDate;          /* start time for build/load              */
  private Date       endDate;            /* end time for build / load              */
  private String     inputFileName;      /* name of the input file being read      */

  /* constructor initializes by setting up the document object */
  IndexLoader (Properties p) {
      configSettings = p;
  }

  /* now lets read the index from the file */
  public InvertedIndex load () {
      FileInputStream istream = null;
      ObjectInputStream p = null;
      InvertedIndex idx = new InvertedIndex();

      startDate = new Date();
      String inputFile = configSettings.getProperty("INDEX_FILE");
      try {
          istream = new FileInputStream(inputFile);
          p = new ObjectInputStream(istream);
      }
      catch (Exception e) {
          System.out.println("Can't open input file ==> " +inputFile);
          e.printStackTrace();
      }

      try {
          idx = (InvertedIndex) p.readObject();
          p.close();
          System.out.println("Inverted index read from  ==> "+inputFile);
      }
      catch (Exception e) {
          System.out.println("Can't read input file ==> "+inputFile);
          e.printStackTrace();
      }
      endDate = new Date();
      return idx;
  }
}