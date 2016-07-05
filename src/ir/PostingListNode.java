//package edu.iit.ir.simpleir;
package ir;
import java.io.*;

/**
 * Title:        New IR System
 * Description:  An entry in a posting list
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author       DG
 * @version 1.0
 */

/* This will store and retreive an entry in a posting list               */
/* typically an entry in a posting list contains the document identifier */
/* and the term frequency                                                */
public class PostingListNode implements Serializable {

    long documentID;
    TermFrequency tf;

    /* simple constructor with no args -- should not be called  */
    public PostingListNode() {
        documentID = 0;
        tf = new TermFrequency();
    }

    /* simple constructor with no args -- should not be called  */
    public PostingListNode(long id, TermFrequency tf) {
        documentID = id;
        this.tf = tf;
    }

    public long getDocumentID() {
        return documentID;
    }

    public short getTF() {
        return tf.getTF();
    }

    public void print() {
        System.out.print ( " (" + documentID + tf.toString() + ") " );
    }


}


