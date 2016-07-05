package ir;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Properties;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Title:        New IR System
 * Description:  Define the attributes, components and events for the GUI
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author       DG
 * @version 1.0
 */

public class SearchOrIndexWindow extends JFrame implements WindowConstants  {

  private final static int MAX_RESULT = 5000;
  private JTextArea      input  = new JTextArea(5,5);
  private JLabel         status = new JLabel("Welcome to Simple IR");
  private JButton        search = new JButton("Search");
  private JButton        index  = new JButton("Create Index");
  private JPanel         contentPane = (JPanel) this.getContentPane();
  private JLabel         prompt = new JLabel("Enter Query Below:");
  private BorderLayout   borderLayout1 = new BorderLayout();
  private IndexBuilder   idxBuilder;
  private IndexLoader    idxLoader;
  private InvertedIndex  idx;
  private QueryProcessor queryProcessor;
  private boolean       indexLoaded = false;
  private Query         query;
  private ResultsWindow resultWindow;

  /* setup for the key GUI window                       */
  /* name the method jbInit so the GUI Builder can help */
  /* we also need the resultWindow object so we can     */
  /* display results                                    */

   public SearchOrIndexWindow(Properties configSettings, ResultsWindow r) {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);  // enable events
        idxBuilder = new IndexBuilder(configSettings);   // create the index builder
        idxLoader  = new IndexLoader(configSettings);    // create the index loader
        queryProcessor = new QueryProcessor(configSettings); // create the query processor
        idx        = new InvertedIndex();
        query = new Query(configSettings);         // create the empty query
        resultWindow = r;

        try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

  /* define and set attributes for all components of the query window */
  private void jbInit() throws Exception {

     /* use the border layout, partitioned into N,S,E,W,Center */
     contentPane.setLayout(borderLayout1);

     /* set the query window size and title */

     this.setTitle("Simple IR Engine");

     /* set up the window listeners */
     this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                this_windowClosed(e);
                /* this is broken */
            }
            public void windowActivated(WindowEvent e) {
                this_windowActivated(e);
            }
        });

    /* now lets set up the attributes for the prompt, search, and query input */
     definePrompt();
     defineQueryInput();
     defineSearchButton();
     defineCreateIndex();
     defineStatus();
  }

  /* search button has been pressed, now return the query */
  private void search_actionPerformed(ActionEvent e) {
     ArrayList results;
     Date startDate;
     Date endDate;

     /* load the index object if necessary */
     if (! indexLoaded) {
        startDate = new Date();
        idx = idxLoader.load();
        endDate = new Date();
        indexLoaded = true;
        System.out.println("Loaded the index");
        displayIndexStatus("Index Loaded.", startDate, endDate);
        idx.print();
     }

       startDate = new Date();
       query.parse(input.getText());
       results = queryProcessor.execute(query, idx);
       Collections.sort(results);
       Collections.reverse(results);
       resultWindow.displayResults(results);
       endDate = new Date();
       displayQueryStatus("Query Completed.", startDate, endDate);
       System.out.println("display result");
  }


  /* create index button has been pressed */
  private void create_index_actionPerformed(ActionEvent e) {
     System.out.println("Build index button pressed. ");
     indexBuilder();
  }

  /* window closed */
  void this_windowClosed(WindowEvent e) {
     System.out.println("Have a happy day");
     System.exit(0);
  }

  /* window activated */
  void this_windowActivated(WindowEvent e) {
      System.out.println("Window Activated -- enter your query");
  }

  /* define the prompt */
  private void definePrompt() {
     prompt.setBackground(Color.white);
     prompt.setFont(new java.awt.Font("Dialog", 1, 22));
     contentPane.add(prompt, BorderLayout.NORTH);
  }

  /* define the search button */
  private void defineSearchButton() {
     search.setBackground(Color.orange);
     search.setFont(new java.awt.Font("Serif", 1, 20));
     search.setForeground(Color.black);
     contentPane.add(search, BorderLayout.EAST);

     /* now lets set up the search action listener */
     search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search_actionPerformed(e);
            }
        });
  }

  /* define the status button */
  private void defineStatus() {
     status.setFont(new java.awt.Font("Dialog", 1, 12));
     contentPane.add(status, BorderLayout.SOUTH);


  }


  /* define the input area for the query */
  private void defineQueryInput() {
     input.setFont(new java.awt.Font("Times New Roman", 3, 24));
     input.setBackground(Color.black);
     input.setForeground(Color.green);
     input.setCaretColor(Color.green);
     input.setLineWrap(true);
     input.setWrapStyleWord(true);
     contentPane.add(input, BorderLayout.CENTER);
  }




  /* define the create index button */
  private void defineCreateIndex() {
     index.setBackground(Color.orange);
     index.setFont(new java.awt.Font("Dialog", 1, 14));
     index.setForeground(Color.black);
     contentPane.add(index, BorderLayout.WEST);

     index.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                create_index_actionPerformed(e);
            }
        });
  }

  /* build the inverted index */
  private void indexBuilder() {

      Date startDate = null;
      Date endDate   = null;

      try {
           startDate = new Date();
           idx = idxBuilder.build();
           endDate = new Date();
      }
      catch (Exception e){
          System.out.println("IndexBuilder: Error building index: " + e);
          e.printStackTrace();
      }

     idx.print();  /* prints the index */

     /* now lets write the inverted index to a file */
     displayIndexStatus("Index Built. ", startDate, endDate );
  }

  private void displayIndexStatus(String inString, Date start, Date end) {
    String timeTaken = SimpleIRUtilities.getElapsedTime(start, end);
    String statusLine = inString +
                        " Elapsed Time: " + timeTaken +
                        " Documents: "  + idx.getNumberOfDocuments();

     status.setText(statusLine);
  }

  private void displayQueryStatus(String inString, Date start, Date end) {
    String timeTaken = SimpleIRUtilities.getElapsedTime(start, end);
    String statusLine = inString + " Elapsed Time: " + timeTaken;
    status.setText(statusLine);
  }
}
