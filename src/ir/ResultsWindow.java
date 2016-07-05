package ir;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.JButton;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JComponent;
import java.text.DecimalFormat;

/**
 * Title:        New IR System
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author
 * @version 1.0
 */

public class ResultsWindow extends JFrame implements WindowConstants  {


    private final int NUM_RESULTS = 10;
    private DocumentDisplayWindow  docDisplayWindow;
    private JPanel        contentPane          = new JPanel();
    private JCheckBox     resultCheckBoxes[]   = new JCheckBox[NUM_RESULTS];
    private JLabel        resultLabels[]       = new JLabel[NUM_RESULTS];
    private JPanel        resultPanels[]       = new JPanel[NUM_RESULTS];
    private LinkedList    documentsSelected    = new LinkedList();
    private ArrayList     currentResult        = new ArrayList();
    private DocumentDisplayWindow  documentDisplayWindow;

    public ResultsWindow(DocumentDisplayWindow d) {

     try {
            jbInit();
            docDisplayWindow = d;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
     }

  /* define and set attributes for all components of the query window */
  private void jbInit() throws Exception {

     String header;
     JButton    displayResultsButton =
          new JButton("Select some documents and press this button to look at them");
     JCheckBox  currentCheckBox;
     JLabel     currentLabel;
     JPanel     currentPanel;
     JPanel     boxHolder;
     FlowLayout layout = new FlowLayout();

     contentPane.setBorder(BorderFactory.createTitledBorder("Results"));
     contentPane.setLayout(new GridLayout(NUM_RESULTS+3, 1, 0, 2));

     /* add a display selected docs button */
     displayResultsButton.setFont(new java.awt.Font("Serif", 1, 14));
     displayResultsButton.setForeground(Color.blue);
     contentPane.add(displayResultsButton);
     displayResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayResultButtonPressed();
            }
        });


     /* add the text header */
     header =    "Select"    + spaces(5) +
                 "Score"     + spaces(6) +
                 "File Name" + spaces(4) +
                 "Doc Name " + spaces(13) +
                 "Dateline"  + spaces(16) +
                 "Title";

      currentLabel = new JLabel(header);
      currentLabel.setFont(new java.awt.Font("Serif", 1, 14));
      currentLabel.setForeground(Color.red);
      contentPane.add(currentLabel);
      layout.setAlignment(FlowLayout.LEFT);

     /* now add the result buttons and labels */
     for (int i=0;  i < NUM_RESULTS ; i++) {

        /* lets define each select button */
        currentCheckBox = new JCheckBox();
        currentCheckBox.setActionCommand(""+i+"");

        resultCheckBoxes[i] = currentCheckBox;
        CheckBoxHandler handler = new CheckBoxHandler();
        resultCheckBoxes[i].addItemListener(handler);

        /* now lets define the label associated with this button */
        currentLabel = new JLabel("");
        currentLabel.setBackground(Color.green);
        currentLabel.setFont(new java.awt.Font("Serif", 1, 14));
        currentLabel.setForeground(Color.black);
        contentPane.add(currentLabel);
        resultLabels[i] = currentLabel;

        currentPanel = new JPanel();
        currentPanel.setLayout(layout);
        currentPanel.add(currentCheckBox);
        currentPanel.add(currentLabel);
        contentPane.add(currentPanel);

     }
     JScrollPane scrollPane = new JScrollPane(contentPane);
     scrollPane.setPreferredSize(new Dimension(400,400));
     setContentPane(scrollPane);
     setVisible(true);



     /* set the query window size and title */
     this.setSize(new Dimension(800,600));
     this.setTitle("Results Window");

     /* set up the window listeners */
     this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
            }
            public void windowActivated(WindowEvent e) {
            }
        });
   }

  /* results are displayed here -- called from search button on query window */
  public void displayResults(ArrayList results) {
        Float         currentScore;
        Document      d;
        int           j;
        Result        r;
        String        resultString;
        DecimalFormat  form = new DecimalFormat("##,###.##");

        System.out.println("Time to Display Results");

        currentResult = results;

        /* initialize the result text */
        for (j = 0; j < NUM_RESULTS; j++) {
           resultLabels[j].setText("");
           resultCheckBoxes[j].setVisible(false);
        }

        /* reset the documents selected list */
        documentsSelected.clear();


        Iterator i = currentResult.iterator();
        j = 0;
        while (i.hasNext()) {
            r = (Result) i.next();
            d = r.getDocument();
            resultString = spaces(8) +
                       r.getScore(5, form)                  + spaces(7) +
                       left(d.getInputFileName(), 15)       + spaces(0) +
                       left(d.getDocName(), 15)             + spaces(2) +
                       left(d.getDateLine(),17)             + spaces(3) +
                       left(d.getTitle(), 50);
            resultLabels[j].setText(resultString);
            resultCheckBoxes[j].setVisible(true);
            ++j;
        }
        setVisible(true);
   }

  /* handle the checkbox. Figure out which one has been   */
  /* pressed.   Add it to the result select list          */
  /* search button has been pressed, now return the query */
  private class CheckBoxHandler implements ItemListener {

    public void itemStateChanged (ItemEvent e)
    {
        int i;

        /* loop to see which checkbox has been modified */
        for (i = 0; i < NUM_RESULTS; i++) {
            if (e.getSource() == resultCheckBoxes[i]) {
                break;
            }
        }

        /* add selected checkbox to selected list */
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("checkbox "+i+" selected");
            documentsSelected.add(currentResult.get(i));
        }

        /* remove selected checkbox */
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            System.out.println("checkbox "+i+" deselected");
            documentsSelected.remove(currentResult.get(i));
        }
    }
  }

  /* now its time to display the result documents */
  private void displayResultButtonPressed() {
       System.out.println("Result Button has been pressed");
       Result   r;
       Document d;

       /* time to send to the display.  Loop through the list to    */
       /* verify we have the correct document info                  */
       docDisplayWindow.displayResultDocuments(documentsSelected);
       System.out.println("Finished viewing results");
  }


  /* returns a string of the first n chars of a string */
  private String left (String input, int size) {

    int i;
    int len;
    String result = "";

    if (input == null) return spaces(size);

    input = input.trim();
    len = input.length();
    if (size > len) {
        result = input + spaces(size - len);
    } else {
        result = input.substring(0, size);
    }
    return result;
  }


  /* returns a string of spaces (useful for formatting) */
  public static String spaces (int numSpaces) {

    int i;
    String result = "";

    for (i = 0; i < numSpaces; i++) {
        result = result + " ";
    }
    return result;
  }

}


