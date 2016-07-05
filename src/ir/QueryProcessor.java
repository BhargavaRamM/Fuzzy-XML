package ir;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Date;
import java.util.Vector;
import java.util.Arrays;
import java.util.TreeMap;
import java.lang.System.*;
import java.util.ArrayList;

/**
 * Title:        New IR System
 * Description:  Takes a query and an inverted index object and produces a result object
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author       DG
 * @version 1.0
 */

public class QueryProcessor {

    Date startDate;              /* save start and end dates to time queries */
    Date endDate;
    int     scType;              /* selected similarity measure              */
    private static final int MAX_DOC = 100000;
    private static final String SC_TYPE[] = {"DOT_PRODUCT"}; /* types of sc's */
    private float  score[] = new float[MAX_DOC];  /* holds query result  */


    /* constructor -- initializes the query */
    public QueryProcessor(Properties configSettings) {
        String scString  = configSettings.getProperty("SC");

        /* convert selected type to numeric indicator */
        boolean scFound = false;
        for (int i = 0; i < SC_TYPE.length; i++) {
            if (SC_TYPE[i].compareTo(scString) == 0) {
                scType = i;
                scFound = true;
            }
        }
        if (! scFound) {
            System.out.println("Invalid similarity measure in config file.");
            System.exit(0);
        }
    }

    /* actually runs the query and returns documents into an arraylist */
    /* really should use a long docid here                             */
    public ArrayList execute(Query query, InvertedIndex idx) {
        int       i;
        ArrayList results = new ArrayList();
        float     currentScore;
        int       docID;
        Result    currentResult;
        Document  currentDocument;

        /* now lets find out what kind of similarity measure to run */
        switch (scType) {
            case 0:
                runDotProduct(query, idx);
                break;
            default:
                System.out.println("Invalid SC Type");
                break;
        }
        printScore();

        /* now lets store the non-zero elements of score in the treemap */
        /* this sets things up nicely for the GUI                       */
        for ( i = 0; i < MAX_DOC; i++) {
            if (score[i] != 0) {
                currentScore = score[i];
                docID = i;
                currentDocument = idx.getDocument(docID);
                currentResult = new Result(currentScore, currentDocument);
                results.add(currentResult);
            }
        }

        return results;
    }

    /* for debugging show contents of score array */
    private void printScore() {
        for (int i = MAX_DOC-1; i >= 0; i--) {
            if (score[i] != 0) {
                System.out.println("Score["+i+"] ==> "+score[i]);
            }
        }
    }

    /* computes dot product similarity measure and puts results */
    /* in the score array                                       */
    private void runDotProduct(Query query, InvertedIndex idx) {
        String          currentToken;
        LinkedList      currentPostingList;
        PostingListNode pln;
        short           qtf;
        int             docID;
        short           tf;
        int             df;
        long            numberOfDocuments;
        double          idf;
        double          idfSquared;
        HashMap         queryTerms = new HashMap();


        numberOfDocuments = idx.getNumberOfDocuments();

        /* initialize the score array */
        for (int i = 0; i < MAX_DOC; i++) {
            score[i] = (float) 0.0;
        }

        /* loop through all query terms, get their posting lists     */
        /* loop through the posting lists and update the score array */
        System.out.println("Updating scores.");
        startDate = new Date();
        queryTerms = query.getTerms();
        Set querySet = queryTerms.keySet();
        Iterator i = querySet.iterator();
        while (i.hasNext()) {
            currentToken = (String) i.next();
            qtf = ((TermFrequency) queryTerms.get(currentToken)).getTF();

            currentPostingList = idx.getPostingList(currentToken);

            /* if no term has no posting list, skip rest of the loop -- no score to update */
            System.out.println("Current Posting List --> " + currentPostingList);
            if (currentPostingList == null) {
                continue;
            }

            df = currentPostingList.size();
            idf = Math.log((double) (numberOfDocuments / df));
            idfSquared = idf * idf;

            Iterator j = currentPostingList.iterator();
            while (j.hasNext()) {
                pln  = (PostingListNode) j.next();
                docID = (int) pln.getDocumentID();
                tf = pln.getTF();
                updateScore(docID,tf,qtf,idfSquared);   /* increase score for document */
            }
        }
        System.out.println("Finished running query");
        endDate = new Date();
    }

    /* here's where we update the score for a document               */
    /* different weighting schemes could easily be incorporated here */
    private void updateScore(int docID, short tf, short qtf, double idfSquared) {

        score[docID] = score[docID] + ((float) tf * (float) qtf * (float) idfSquared) ;
        System.out.println("tf --> " + tf);
        System.out.println("qtf --> " + qtf);
        System.out.println("idfSquared --> " + idfSquared);
        System.out.println("score["+docID+"] ==> "+score[docID]);

        return;
    }
}

