package ir;
import  java.util.ArrayList;
import  java.io.IOException;
import  java.io.InputStream;
import  java.io.FileInputStream;
import  java.io.FileNotFoundException;
import  java.io.BufferedReader;
import  java.io.InputStreamReader;
import  java.io.Reader;
import  java.util.HashMap;

/**
 * Title:        New IR System
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author
 * @version 1.0
 */

public class TRECParser {

    private static final int  BEGIN_DOC      =   0;
    private static final int  END_DOC        =   1;
    private static final int  BEGIN_TITLE    =   2;
    private static final int  END_TITLE      =   3;
    private static final int  BEGIN_TEXT     =   4;
    private static final int  END_TEXT       =   5;
    private static final int  BEGIN_DOC_NAME =   6;
    private static final int  END_DOC_NAME   =   7;
    private static final int  BEGIN_DATELINE =   8;
    private static final int  END_DATELINE   =   9;
    private static final int  WORD           =  -3;

    private HashMap               distinctTerms;
    private SimpleStreamTokenizer in;
    private String                inputFileName;
    private StopWordList          stopWords;
    // private PorterStemmer         porterStemmer;
    // private LovinsStemmer         lovinsStemmer;

    TRECParser (String ifn, String swfn) {
        inputFileName = ifn;
        in = parserInit();
        stopWords = new StopWordList(swfn);

        /* activiate the Porter stemmer */
        // porterStemmer = new PorterStemmer();

        /* activiate the Lovins stemmer */
        //lovinsStemmer = new LovinsStemmer();

    }

    /* sets up for reading the inverted index: opens the input stream and */
    /* reads in the stop word list                                        */
    private SimpleStreamTokenizer parserInit() {

         SimpleStreamTokenizer in = null;

        /* see if we can open the input file */
        try {
            /* open the input stream for the input file */
            InputStream is = new FileInputStream(inputFileName);

            /* now make sure the input reading is buffered */
            Reader r = new BufferedReader ( new InputStreamReader (is));

            /* now set up the stream tokenizer which will tokenize this */
            HashMap tags = initTags();
            in = new SimpleStreamTokenizer (r, tags);
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not open file --> "+inputFileName);
            System.exit(1);
        }
        return in;
  }

  /* this will read the file of documents, parse them and return a list of document */
  /* objects.  Could be called the document factory since it generates useful       */
  /* document objects that can then be indexed.                                     */
  /* Documents are created when an end of document tag is encountered.              */
  public ArrayList readDocuments() {

      String      dateline     = null;
      String      docName      = null;
      ArrayList   documentList = new ArrayList();
      boolean     done         = false;
      int         documentID   = 0;
      int         length       = 0;
      int         offset       = 0;
      String      title        = null;


      boolean endOfFile = false;
      while (! done) {

          System.out.println("Token --> " + in.sval);
          /* check to see if we hit the end of the file */
          try {
              if (in.nextToken() == in.TT_EOF) {
                  done = true;
                  endOfFile = true;
                  continue;
              }

              /* now test for a tag */
              switch (in.ttype) {
	        case END_DOC:
                        /* when we hit the end of a document, lets create a new object */
                        /* to store info needed for indexing                           */
                        length = in.currentPosition - offset;
                        Document d = new Document (documentID, docName, title, dateline,
                                         inputFileName, offset, length, distinctTerms);
                        ++documentID;
                        documentList.add(d);
                        break;

                /* initialize document attributes when we start a new doc */
                case BEGIN_DOC:
                        System.out.println("Started new document");
                        offset = in.currentPosition;
                        docName = null;
                        dateline = null;
                        title = null;
                        distinctTerms = new HashMap();
                        break;

	        case BEGIN_DOC_NAME:
	                docName = readNoIndex(in, END_DOC_NAME);
                        break;

	        case BEGIN_TITLE:
                        title = readNoIndex(in, END_TITLE);
                        break;

                case BEGIN_DATELINE:
                        dateline = readNoIndex(in, END_DATELINE);
                        break;

                case BEGIN_TEXT:
                        readText(in, stopWords);
                        break;

                /* only time we get a word here is if its outside of any tags */
                case WORD:
                        break;

	        default: {
                    System.out.println("Unrecognized tag: "+in.sval);
                    System.exit(-1);
                }
            }
          } catch (IOException e) {
                  System.out.println("Exception while reading doc ");
                  e.printStackTrace();
          }
      }
      return documentList;
   }

  /* scans the document for the title */
  private String readNoIndex(SimpleStreamTokenizer in, int endTag) throws IOException {

        String text = "";
        String token;

        in.nextToken();
        in.wordChars(',',',');   /* include commas for non indexed stuff */
        token = in.sval;
        while (in.ttype != endTag) {
              text = text + " " + token;
              in.nextToken();
              token = in.sval;

        }
        in.whitespaceChars(',',','); /* reset comma as delimiter */
        return text;
  }

  /* reads the material marked with TEXT delimiters  */
  /* this is the data that is actually indexed       */
  private void readText(SimpleStreamTokenizer in, StopWordList stopWords) throws IOException {

    TermFrequency tf;
    String token;

    in.nextToken();
    token = in.sval;
    while (in.ttype != END_TEXT) {
        token = token.toLowerCase();
        if (! stopWords.contains(token)) {   // skip stop words

            // good time to stem is here
            //String porterStem = porterStemmer.runStemmer(token);
            //String lovinsStem = lovinsStemmer.stemString(token);
            if (distinctTerms.containsKey(token)) {
                tf = (TermFrequency) distinctTerms.get(token);
                tf.Increment();                 // if we have it just increment tf
                distinctTerms.put(token, tf);
            } else {
                tf = new TermFrequency();       // new token, init the tf
                distinctTerms.put(token,tf);
            }
        }
        in.nextToken();
        token = in.sval;
    }
    System.out.println("Hit end of text");
    return;
  }


   /** returns a hashmap of initialized tags
    *  This way we can have more than one tagstring map to the same
    *  tag type.  For example, we might have DOCID and DOCUMENTID both
    *  map to the BEGIN_DOCID tag.
    */
   private HashMap initTags() {

        int NUM_TAGS = 10;
        Integer tagVals[] = new Integer[NUM_TAGS];
        HashMap tags = new HashMap();

        for (int i = 0; i < NUM_TAGS; i++) {
            tagVals[i] = new Integer(i);
        }

        tags.put("<DOC>",             tagVals[0]);
        tags.put("</DOC>",            tagVals[1]);


        tags.put("<TITLE>",           tagVals[2]);
        tags.put("<TL>",              tagVals[2]);
        tags.put("<HEADLINE>",        tagVals[2]);

        tags.put("</TITLE>",          tagVals[3]);
        tags.put("</TL>",             tagVals[3]);
        tags.put("</HEADLINE>",       tagVals[3]);

        tags.put("<TEXT>",            tagVals[4]);
        tags.put("</TEXT>",           tagVals[5]);
        tags.put("<DOCID>",           tagVals[6]);
        tags.put("<DOCNO>",           tagVals[6]);

        tags.put("</DOCID>",          tagVals[7]);
        tags.put("</DOCNO>",          tagVals[7]);
        tags.put("<DATELINE>",        tagVals[8]);
        tags.put("</DATELINE>",       tagVals[9]);
        return tags;
   }
}