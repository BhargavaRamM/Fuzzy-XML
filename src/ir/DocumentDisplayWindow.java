package ir;
import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Iterator;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Color;


/**
 * Title:        New IR System
 * Description:  Document Display Window
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author       DG
 * @version 1.0
 */

public class DocumentDisplayWindow extends JFrame implements WindowConstants {

    private final int NUM_RESULTS = 10;
    private JPanel        contentPane          = new JPanel();
    private JEditorPane   resultText           = new JEditorPane();
    public DocumentDisplayWindow() {

     try {
            jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
     }

  /* define and set attributes for all components of the query window */
  private void jbInit() throws Exception {

     String header;
     FlowLayout layout = new FlowLayout(FlowLayout.LEFT);

     contentPane.setBorder(BorderFactory.createTitledBorder("Selected Documents"));
     contentPane.setLayout(layout);
     contentPane.setBackground(Color.white);
     resultText.setFont(new java.awt.Font("Times New Roman",1,12));
     resultText.setContentType("text/html");

     JScrollPane scrollPane = new JScrollPane(resultText);
     scrollPane.setPreferredSize(new Dimension(450,500));
     contentPane.add(scrollPane);
     setContentPane(contentPane);



     /* set the query window size and title */
     this.setTitle("Selected Document Results Window");

     /* set up the window listeners */
     this.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
            }
            public void windowActivated(WindowEvent e) {
            }
        });
   }

  /* results are displayed here -- called from search button on query window */
  public void displayResultDocuments(LinkedList documentsSelected) {
        Document  d;
        int       j;
        Result    r;
        String    res = "<html><body><pre>";

        Iterator i = documentsSelected.iterator();
        while (i.hasNext()) {
             r = (Result) i.next();
             d = r.getDocument();
             res = res + d.getText() + "\n\n";
             System.out.println("res --> "+res);
        }
        res = res + "</pre></body></html>";
        resultText.setText(res);
        pack();
        setVisible(true);
  }

}