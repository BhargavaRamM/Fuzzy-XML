package ir;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;
import ir.SimpleStreamTokenizer;

/* this class actually can build the inverted index */
public class IndexBuilder  implements java.io.Serializable {

  private Properties configSettings;     /* config settings object                 */
  private int        numberOfDocuments;  /* num docs in the index                  */
  private String     inputFileName;      /* name of the input file being read      */
  private ArrayList  documentList = new ArrayList();

  /* constructor initializes by setting up the document object */
  IndexBuilder (Properties p) {
      configSettings = p;
  }

  /* build the inverted index, reads all documents */
  public InvertedIndex build () throws IOException {

    boolean endOfFile = false;
    int offset = 0;
    Document d;

    InvertedIndex index = new InvertedIndex();
    index.clear();

    /* get the name of the input file to index */
    inputFileName = configSettings.getProperty("TEXT_FILE");

    /* now read in the stop word list */
    String stopWordFileName = configSettings.getProperty("STOPWORD_FILE");

    /* now lets parse the input file */
    TRECParser parser = new TRECParser(inputFileName, stopWordFileName);
    documentList = parser.readDocuments();

    /* loop through the list of documents and add each one to the index */
    Iterator i = documentList.iterator();
    System.out.println("Starting to build the index");
    while (i.hasNext()) {
         d = (Document) i.next();
         System.out.println("Adding document " + d.getDocumentID());
         System.out.println("about to add these terms to index: ");
         d.printTerms();
         index.add(d);
    }
    index.print();

    /* now lets save the index to a file */
    index.write(configSettings);  // write the index to a file
    return index;
  }


}