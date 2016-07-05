package ir;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;
import ir.SimpleStreamTokenizer;

/* actual inverted index object -- built by the index builder -- loaded from disk */
/* by the index loader                                                            */

public class InvertedIndex implements java.io.Serializable {

  private Properties configSettings;     /* config settings object                 */
  private HashMap    index;              /* actual inverted index                  */
  private ArrayList  documentList;       /* stores structured info about each doc  */
  private String     inputFileName;      /* name of the input file being read      */
  private HashMap    dictionary;         /* list of terms and their df             */

  /* constructor initializes by setting up the document object */
  InvertedIndex () {
      index = new HashMap();
      documentList = new ArrayList();
  }

  /* add terms to the index                                            */
  /* index is really just a hashmap of terms that map to posting lists */
  public void add(Document d) {
      String token;  /* string to hold current token          */
      TermFrequency tf;  /* holds current term frequency      */
      Set termSet;       /* set of terms to add to index      */
      LinkedList postingList;  /* list of docID, tf entries         */
      HashMap terms = new HashMap();
      Integer documentFrequency;
      int     df;


      /* add all the terms in the document to the index */
      long docID = d.getDocumentID();  // current document ID
      terms = d.getTerms();       // get current term map
      System.out.println("in inverted index --- terms" + terms.toString());

      /* obtain set of distinct terms to add */
      termSet = terms.keySet();

      /* now lets have an iterator for this set */
      Iterator i = termSet.iterator();

      /* loop through the terms and add them to the index */
      while (i.hasNext()) {
          token = (String) i.next();

          /* if we have the term, just get the existing posting list  */
          if (index.containsKey(token)) {
             postingList = (LinkedList) index.get(token);
          } else {
             /* otherwise, add the term and then create new posting list */
             postingList = new LinkedList();
             index.put(token, postingList);
          }

          /* now add this node to the posting list */
          tf = (TermFrequency) terms.get(token);
          PostingListNode currentNode = new PostingListNode(docID, tf);
          postingList.add(currentNode);
      }

      /* keep track of which documents have been indexed */
      documentList.add(d);

      /* debugging */
      d.printTitle();
      d.printTerms();
  }

  /* clear the inverted index */
  public void clear () {
        index.clear();
        documentList.clear();
  }


  /* this will the index to disk */
  public void write(Properties configSettings) {
      FileOutputStream ostream = null;
      ObjectOutputStream p = null;

      String outputFile = configSettings.getProperty("INDEX_FILE");
      try {
          ostream = new FileOutputStream(outputFile);
          p = new ObjectOutputStream(ostream);
      }
      catch (Exception e) {
          System.out.println("Can't open output file.");
          e.printStackTrace();
      }

      try {
          p.writeObject(this);
          p.flush();
          p.close();
          System.out.println("Inverted index written to file ==> "+outputFile);
      }
      catch (Exception e) {
          System.out.println("Can't write output file.");
          e.printStackTrace();
      }
  }

  public long getNumberOfDocuments() {
    return documentList.size();
  }

  /* return a document object given its id */
  public Document getDocument(int documentID) {
        Document d = (Document) documentList.get(documentID);
        return d;
  }

  /* sets the list of documents -- created by the parser */
  public void setDocumentList (ArrayList dl) {
        documentList = dl;
  }

  /* returns a posting list for a given term */
  public LinkedList getPostingList(String token) {
    LinkedList result = new LinkedList();

    if (index.containsKey(token)) {
        result = (LinkedList) index.get(token);
    } else {
        result = null;
    }
    return result;
  }

  /* debugging method prints contents of inverted index */
  public void print() {
      String token;  /* string to hold current token          */
      TermFrequency tf;  /* holds current term frequency      */
      Set termSet;       /* set of terms to add to index      */
      LinkedList postingList;  /* list of docID, tf entries         */
      PostingListNode pln;     /* current posting list node */

      /* obtain set of distinct terms to add */
      termSet = index.keySet();

      /* now lets have an iterator for this set */
      Iterator i = termSet.iterator();

      /* loop through the terms and add them to the index */
      while (i.hasNext()) {
          token = (String) i.next();
          System.out.print(SimpleIRUtilities.padToken(token) + ":");

          /* now traverse posting list */
          postingList = (LinkedList) index.get(token);
          Iterator j = postingList.iterator();
          while (j.hasNext()) {
              pln = (PostingListNode) j.next();
              pln.print();
          }
          System.out.println();
      }
    }
}


