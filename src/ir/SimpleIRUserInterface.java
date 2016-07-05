package ir;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.UIManager;


/**
 * Title:        New IR System
 * Description:  This launches the whole user interface
 *               reads the configuration file and sets up the main GUI windows
 * Copyright:    Copyright (c) 2000
 * Company:      IIT
 * @author DG
 * @version 1.0
 */

public class SimpleIRUserInterface  {

      public static void main (String args[]) {

            if (args.length !=  1) {
                System.out.println ("Usage: UserInterface <config file>");
                System.exit(1);
            }

            Properties configSettings = defineProperties(args[0]);
            defineMainGUIWindows(configSettings);
        }

      /* this reads the config file and defines the properties */
      private static Properties defineProperties(String propertyFileName) {

            Properties configSettings = new Properties();
            try {
                configSettings.load(new FileInputStream(propertyFileName));
            }
            catch (Exception e) {
                System.out.println("Could not load config file "+propertyFileName);
                e.printStackTrace();
            }
            return configSettings;
      }

      /* start the GUI */
      private static void defineMainGUIWindows(Properties configSettings) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e)  {
                System.out.println("Unable to set look and feel");
            }

            /* setup the document display window */
            DocumentDisplayWindow d = new DocumentDisplayWindow();
            d.setLocation(551,0);
            d.setSize(new Dimension(451,700));
            d.setVisible(false);

            /* setup the result window */
            ResultsWindow r = new ResultsWindow(d);
            r.setLocation(0, 250);
            r.setSize(new Dimension(800,450));
            r.setVisible(false);

            /* setup the query window */
            SearchOrIndexWindow s = new SearchOrIndexWindow(configSettings, r);
            s.setLocation(0,0);
            s.setSize(new Dimension(550,250));
            s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            s.setVisible(true);
        }
    }
