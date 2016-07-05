package ir;
import java.io.Reader;
import java.io.InputStream;
import java.io.IOException;
import java.util.HashMap;

/* essentially the java.io.StreamTokenizer, but number recognition is removed from */
/* getToken to speed up parsing. */

public class SimpleStreamTokenizer {


    /* Only one of these will be non-null */

    private Reader reader = null;
    private HashMap tags = new HashMap();
    private InputStream input = null;
    private char buf[] = new char[20];
    public  int ttype;  /* token type */
    private Integer ttypeInt = new Integer(0); /* integer object needed for tagtype id */

    private static final int SKIP_LF = Integer.MAX_VALUE - 1;

    private byte ctype[] = new byte[256];
    private static final byte CT_WHITESPACE = 1;
    private static final byte CT_ALPHA = 4;
    public short currentPosition = 0;

    /**
     * A constant indicating that the end of the stream has been read.
     */
    public static final int TT_EOF = -1;

    /**
     * A constant indicating that the end of the line has been read.
     */
    public static final int TT_EOL = '\n';

    /**
     * A constant indicating that a word token has been read.
     */
    public static final int TT_WORD = -3;

    /**
     * Value of the token
     */
    public String sval;

    /** Private constructor that initializes everything except the streams. */
    private SimpleStreamTokenizer() {
	wordChars('a', 'z');
	wordChars('A', 'Z');
	wordChars(128 + 32, 255);
	whitespaceChars(0, ' ');
        whitespaceChars('.','.');

	wordChars('/','/');
        wordChars('\\','\\');
        wordChars('<','<');
        wordChars('>','>');
        wordChars('\'','\'');
        wordChars('0','9');
        wordChars('-','-');
    }

    /**
     * Create a tokenizer that parses the given character stream.
     *
     * @param r  a Reader object providing the input stream.
     * @since   JDK1.1
     */
    public SimpleStreamTokenizer(Reader r, HashMap parserTags) {
	this();
        if (r == null) {
            throw new NullPointerException();
        }
	reader = r;
        tags = parserTags;
    }

    /**
     * Set all chars in the ascii range from low to hi to be simple alphabetic
     * characters (not delimiters).
     *
     * @param   low   the low end of the range.
     * @param   hi    the high end of the range.
     */
    public void wordChars(int low, int hi) {
	while (low <= hi)
	    ctype[low++] |= CT_ALPHA;
    }

    /**
     * set all characters in the range to whitespace characters -- treated
     * as delimiters
     *
     * @param   low   the low end of the range.
     * @param   hi    the high end of the range.
     */
    public void whitespaceChars(int low, int hi) {
	while (low <= hi)
	    ctype[low++] = CT_WHITESPACE;
    }

    /** Read the next character */
    private int read() throws IOException {
	++currentPosition;
        return reader.read();
    }

    /**
     * Parses the next token from the input stream of this tokenizer.
     * Simple reads characters until a whitespace character is encountered
     *
     * The assumption is that the syntax table which maps characters to
     * alphabetic or whitespace is setup and the parser will not keep
     * looping on this until TT_EOF is returned
     *
     */
    public int nextToken() throws IOException {
	sval = null;
        boolean tag = false;
        int currentCharacterType;

        /* read a character */
        int c = read();

        /* check for end of file */
        if (c < 0)
            return ttype = TT_EOF;

	ttype = c;		/* Just to be safe */

        /* lets find out what kind of char we have -- if its outside of */
        currentCharacterType = ctype[c];

        /* skip over any initial useless chars */
        while (currentCharacterType == CT_WHITESPACE) {
	    c = read();

            /* check for EOF */
            if (c < 0) {
		return ttype = TT_EOF;
            }

	    currentCharacterType = ctype[c];
	}

        /* lets see if we have a tag */
        if (c == '<') {
            tag = true;
        }

        /* now lets grab text and store it in the buffer    */
        /* the buffer expands if we get a really long token */
	if ((currentCharacterType == CT_ALPHA)) {
	    int i = 0;
	    do {
                // gradually increase the token buffer if necessary
		if (i >= buf.length) {
		    char nb[] = new char[buf.length * 2];
		    System.arraycopy(buf, 0, nb, 0, buf.length);
		    buf = nb;
		}
		buf[i++] = (char) c;
		c = read();
		currentCharacterType = ctype[c];
	    } while (currentCharacterType == CT_ALPHA);
	    sval = String.copyValueOf(buf, 0, i);

            /* if we have a tag, lets return its type -- */
            if (tag) {
                ttypeInt = (Integer) tags.get(sval);
                return ttype = ttypeInt.intValue();
            } else {
        	return ttype = TT_WORD;
            }
	}
        return ttype;
    }


    /**
     * Returns the string representation of the current stream token.
     *
     * @return  a string representation of the token specified by the
     *          <code>ttype</code>, <code>nval</code>, and <code>sval</code>
     *          fields.
     * @see     java.io.StreamTokenizer#nval
     * @see     java.io.StreamTokenizer#sval
     * @see     java.io.StreamTokenizer#ttype
     */
    public String toString() {
	String ret;
	switch (ttype) {
	  case TT_EOF:
	    ret = "EOF";
	    break;
	  case TT_EOL:
	    ret = "EOL";
	    break;
	  case TT_WORD:
            System.out.println("Regular word");
	    ret = sval;
	    break;
	  default: {
                System.out.println("Strange Token");
		/*
		 * ttype is the first character of either a quoted string or
		 * is an ordinary character. ttype can definitely not be less
		 * than 0, since those are reserved values used in the previous
		 * case statements
		 */
		char s[] = new char[3];
		s[0] = s[2] = '\'';
		s[1] = (char) ttype;
		ret = new String(s);
		break;
	    }
	}
	return "Token[" + ret + "]" ;
    }
}
