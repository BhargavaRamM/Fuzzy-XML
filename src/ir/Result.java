package ir;
import java.text.DecimalFormat;

/**
 * Title:        New IR System
 * Description:  A single result, consists of a score, document identifier and a title
 *
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author
 * @version 1.0
 */

public class Result implements Comparable {

    private float score;
    private Document currentDocument;
    private static final int MAX_LENGTH = 60;


    public Result(float s, Document d) {
        score = s;
        currentDocument = d;
    }

    public void put (float s, Document d) {
        score = s;
        currentDocument = d;
    }

    /* returns right justified char string representation of score */
    /* with d digits                                               */
    public String getScore (int d, DecimalFormat form) {
        String temp;
        String result;
        Float  f;

        f = new Float(score);
        temp = form.format(f);
        return ResultsWindow.spaces(d - temp.length()) + temp;
    }

    public Document getDocument () {
        return currentDocument;
    }

    public int compareTo (Object r) {
        Result currentResult = (Result) r;
        if (currentResult.score > score) return -1;

        if (currentResult.score < score) return 1;

        return 0;
    }
}
