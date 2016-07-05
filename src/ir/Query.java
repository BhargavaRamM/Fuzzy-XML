package ir;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Date;
import java.util.Vector;
import java.util.Arrays;
import java.util.TreeMap;
import java.io.FileInputStream;
import java.lang.System.*;
import java.util.ArrayList;

/**
 * Title:        New IR System
 * Description:  qery Object -- parses query, runs the query
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author       DG
 * @version 1.0
 */

public class Query {
    HashMap query = new HashMap();
    short numberOfTokens = 0;    /* size of the query                        */
    StopWordList stopWordList;   /* stop word list for this document         */
    Date startDate;              /* save start and end dates to time queries */
    Date endDate;
    int     scType;              /* selected similarity measure              */
    private static final int MAX_DOC = 100000;
    private static final String SC_TYPE[] = {"DOT_PRODUCT"}; /* types of sc's */
    private float  score[] = new float[MAX_DOC];  /* holds query result  */


    /* constructor -- initializes the query */
    public Query(Properties configSettings) {
        stopWordList = new StopWordList(configSettings.getProperty("QUERY_STOPWORD_FILE"));
    }

    /* parse query into a hashmap that contains each term and for the term */
    /* its query term frequency                                            */
    public void parse(String queryString) {
        StringTokenizer s = new StringTokenizer(queryString);
        String token;
        TermFrequency tf;

        query.clear();
        while (s.hasMoreTokens()) {
            token = (String) s.nextToken();
            token = token.toLowerCase();
            System.out.println("Processing Query Token --> "+token);
            ++numberOfTokens;
            if (! stopWordList.contains(token)) {   // skip stop words
                if (query.containsKey(token)) {
                  tf = (TermFrequency) query.get(token);
                  tf.Increment();                 // if we have it just increment tf
                  query.put(token, tf);
                } else {
                  tf = new TermFrequency();       // new token, init the tf
                  query.put(token,tf);
                }
            }
        }
    }


    /* prints the query tokens for debugging */
    public void print() {
        String token;  /* string to hold current token          */
        TermFrequency tf;  /* holds current term frequency      */

        /* get the set of terms in this document */
        Set querySet = query.keySet();
        Iterator i = querySet.iterator();
        while (i.hasNext()) {
            token = (String) i.next();
            tf = (TermFrequency) query.get(token);
            System.out.println("Query Term ==> "+token + "   tf ==> "+tf.toString());
        }
    }

    /* obtain the hashmap that returns the query */
    public HashMap getTerms() {
        return query;
    }


}