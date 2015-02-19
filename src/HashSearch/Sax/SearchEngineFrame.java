/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HashSearch.Sax;

import HashSearch.AccessDB.AccessFTDB;
import HashSearch.AccessDB.AccessDSPDB;
import HashSearch.AccessDB.AccessNLDB;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import HashSearch.Entities.SetS;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author angele
 */
public class SearchEngineFrame extends javax.swing.JFrame {

    private static AccessFTDB accessFTDB;
    private static AccessDSPDB accessDSPDB;
    private static AccessNLDB accessNLDB;
    private static boolean create = true;
    private static String rapport = "";

    /**
     * Creates new form SearchEngineFrame
     */
    public SearchEngineFrame() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setTitle("Program Hash Search");
        jt_results.setEditable(false);
        jt_errors.setEditable(false);
        jt_errors.setForeground(Color.red);
        this.setResizable(false);
    }

    /**
     * This method allows to access to the database FrequencyTable
     */
    public static void accessToFrequencyTableDB() {
        boolean removeExistingDatabase = false;
        String databaseName = "databases/frequencyTable.db";
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setTransactional(true);
            envConfig.setInitializeCache(true);
            envConfig.setInitializeLocking(true);
            //     envConfig.setInitialMutexes(1000000000);
            envConfig.setMaxLockers(10000);
            if (create) {
                envConfig.setAllowCreate(true);
            }
            Environment env = new Environment(new File("."), envConfig);
            // Remove the previous database.
            if (removeExistingDatabase) {
                env.removeDatabase(null, databaseName, null);
            }

            // create the app and run it
            accessFTDB = new AccessFTDB(env, databaseName);
            accessFTDB.run();

        } catch (DatabaseException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method allows to access to the database DSP
     */
    public static void accessToDSPDB() {
        boolean removeExistingDatabase = false;
        String databaseName = "databases/DSP.db";
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setTransactional(true);
            envConfig.setInitializeCache(true);
            envConfig.setInitializeLocking(true);
            //  envConfig.setInitialMutexes(1000000000);
            envConfig.setMaxLockers(10000);
            if (create) {
                envConfig.setAllowCreate(true);
            }
            Environment env = new Environment(new File("."), envConfig);
            // Remove the previous database.
            if (removeExistingDatabase) {
                env.removeDatabase(null, databaseName, null);
            }

            // create the app and run it
            accessDSPDB = new AccessDSPDB(env, databaseName);
            accessDSPDB.run();

        } catch (DatabaseException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method allows to access to the database NL
     */
    public static void accessToNLDB() {

        boolean removeExistingDatabase = false;
        String databaseName = "databases/NL.db";
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setTransactional(true);
            envConfig.setInitializeCache(true);
            envConfig.setInitializeLocking(true);
            // envConfig.setInitialMutexes(1000000000);
            envConfig.setMaxLockers(10000);
            if (create) {
                envConfig.setAllowCreate(true);
            }
            Environment env = new Environment(new File("."), envConfig);
            // Remove the previous database.
            if (removeExistingDatabase) {
                env.removeDatabase(null, databaseName, null);
            }

            // create the app and run it
            accessNLDB = new AccessNLDB(env, databaseName);
            accessNLDB.run();

        } catch (DatabaseException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * This method close all the database
     *
     * @throws DatabaseException
     */
    public static void closeAllTheDatabases() throws DatabaseException {
        accessFTDB.close();
        accessDSPDB.close();
        accessNLDB.close();
    }

    /**
     * This method write in a file what contains the map only
     *
     * @param nameFile
     * @param text
     */
    public static void writeInFile(String nameFile, String text) {
        String adressedufichier = "rapports/" + nameFile;
        rapport += " Write in the file " + nameFile;
        try {
            FileWriter fw = new FileWriter(adressedufichier, false);
            BufferedWriter output = new BufferedWriter(fw);
            output.write(text);
            output.flush();
            output.close();
            System.out.println("File created");
        } catch (IOException ioe) {
            System.out.print("Error : ");
            ioe.printStackTrace();
        }

    }

    /**
     * Method which find the keyword with the lowest frequency in the frequency
     * table
     *
     * @param keywords
     * @return SetS<String, Integer>
     */
    public static SetS getTheLowestFrequency(List<String> keywords) throws Exception {
        int frequency = 0;
        SetS set = new SetS();
        try {
            frequency = Integer.parseInt(accessFTDB.findValueThanksToKey(keywords.get(frequency)));
            for (String keyword : keywords) {
                if (Integer.parseInt(accessFTDB.findValueThanksToKey(keyword)) <= frequency) {
                    frequency = Integer.parseInt(accessFTDB.findValueThanksToKey(keyword));
                    set = new SetS(keyword, frequency);
                }
            }
        } catch (NumberFormatException e) {

        }

        // System.out.println("The keyword with the lowest frequency : " + "'" + set.getKeyword()
        //          + "'" + " with a frequency of : " + set.getPreorderid());
        return set;
    }

    /**
     * This method extracts the digits from a String composed of nodes and
     * digits
     *
     * @param preordersID
     * @return List<Integer>
     */
    public static List<Integer> extractDigitsFromPreorderID(String preordersID) throws Exception {
        /*
         We separate the string in many little strings
         and we select only the words or numbers
         */
        List<Integer> digits = new ArrayList();
        try {
            Pattern p = Pattern.compile("\\W");
            String[] items = p.split(preordersID, 100);
            for (String item : items) {
                if (!item.equals("")) {
                    digits.add(Integer.parseInt(item));
                }
            }
        } catch (Exception e) {

        }
        return digits;
    }

    /**
     * Extract the different path of nodes
     * @param nodeList
     * @return List<String>
     */
    public static List<String> extractListFromNL(String nodeList) {
        List<String> listOfPathNodes = new ArrayList();
        /*
         We separate the list of numbers in little
         list of numbers
         */
        StringTokenizer st2 = new StringTokenizer(nodeList, ",");

        while (st2.hasMoreElements()) {
            listOfPathNodes.add(st2.nextElement().toString());
        }

        return listOfPathNodes;

    }

    /**
     * Method written litteraly according to the algorithm written in the paper
     * hash search
     *
     * @param anc
     * @param keywords
     * @param preordersID
     * @return
     */
    public static String limitedSLCA(int anc, List<String> keywords, String preordersID) throws Exception {
        if (anc == 0 || keywords == null || keywords.isEmpty()
                || preordersID == null || preordersID.equalsIgnoreCase("")) {
            return "Entrer dans des valeurs valides pour"
                    + " effectuer le limitedSLCA.";
        }
        System.out.println("Limited SLCA -- test for : " + preordersID);
        String limitedslca = "";
        String result = "";
        boolean LCA = true;
        try {
            List<Integer> idArray = extractDigitsFromPreorderID(preordersID);
            Collections.sort(idArray);
            int lastPosition = 0;
            for (int i = 0; i < idArray.size(); i++) {
                lastPosition = i;
            }
            SetS setExample = new SetS("Database", 3);

            int left = idArray.indexOf(anc);
            int right = lastPosition;
            int position = left;
            for (String keyword : keywords) {
                SetS setDSP = new SetS(keyword, idArray.get(left + 1));
                if (!accessDSPDB.verifyIfDBcontainsTheKeyAndValue(setDSP)) {
                    LCA = false;
                }
            }
            if (!LCA) {
                return limitedslca = "[" + String.valueOf(anc) + "]";
            }
            while (left < right) {
                int mid = (left + right) / 2;

                for (String keyword : keywords) {
                    SetS setDSP = new SetS(keyword, idArray.get(mid));
                    if (!accessDSPDB.verifyIfDBcontainsTheKeyAndValue(setDSP)) {
                        LCA = false;
                    }
                }

                if (LCA) {

                    left = mid + 1;
                } else {
                    right = mid - 1;
                }

            }
            if (left < position) {
                return null;
            } else {
                // limitedslca = String.valueOf(idArray.get(left - 1));
                limitedslca = String.valueOf(idArray.get(left - 1));
            }
            //  System.out.println("limited slca : " + limitedslca);
            for (int i = 0; i < limitedslca.length(); i++) {
                result = preordersID.substring(0, preordersID.indexOf(limitedslca) + limitedslca.length()) + "]";
            }
        } catch (Exception e) {

        }

        return result;
    }

    /**
     * Method which allows to find the limited slca for a list of keywords and
     * thanks to a node like a reference
     *
     * @param anc
     * @param keywords
     * @param preordersID
     * @return String
     */
    public static String limitedSLCA_versionWhithoutBinarySearch(int anc, List<String> keywords, String preordersID) throws Exception {
        System.out.println("Limited limitedSLCA_versionWhithoutBinarySearch -- test for : " + preordersID);
        String result = "";
        List<Integer> idArray = extractDigitsFromPreorderID(preordersID);
        Collections.sort(idArray);
        boolean isOK = true;
        int theID = 0;
        SetS setValid = new SetS(keywords.get(0), idArray.get(0));
        theID = idArray.get(0);
        for (Integer idArray1 : idArray) {
            for (String keyword : keywords) {
                if (isOK && accessDSPDB.verifyIfDBcontainsTheKeyAndValue(new SetS(keyword, idArray1))) {
                    isOK = true;
                } else {
                    isOK = false;
                }
            }
            if (isOK) {
                theID = idArray1;
            }
        }
        Pattern pID = Pattern.compile(".*" + theID);
        Matcher mID = pID.matcher(preordersID);
        while (mID.find()) {
            if (theID == 1) {
                //  System.out.println(theID);
                //  System.out.println(" l" + mID.group().substring(0, theID));
                result = mID.group().substring(0, theID + 1) + "]";

            } else {
                result = mID.group() + "]";
            }
        }
        return result;
    }

    /**
     *
     * @param keywords
     * @return String
     * @throws java.lang.Exception
     */
    public static String hashSearch(List<String> keywords) throws Exception {
        System.out.println("Hash search : ");
        SetS f1 = new SetS();
        String result = "";
        try {
            if (keywords == null) {
                return "The keywords in input are not correct.";
            }
            f1 = getTheLowestFrequency(keywords);
            String NLOfkeyword = accessNLDB.findValueThanksToKey(f1.getKeyword());

            for (String keyword : keywords) {
                if (!keyword.equalsIgnoreCase(f1.getKeyword())) {
                    String path = accessNLDB.findValueThanksToKey(keyword);
                }
            }

            List<String> S1 = extractListFromNL(NLOfkeyword);
            int v = 1;
            for (String node : S1) {
                String X = limitedSLCA(1, keywords, node);
                String preX = "";
                if (X.length() == 3) {
                    preX = "1";
                    result = "[" + preX + "]";
                } else {
                    for (int i = 0; i < X.length(); i++) {
                        preX = X.substring(X.lastIndexOf(".") + 1, X.indexOf("]"));
                    }
                }
                if (v < Integer.parseInt(preX)) {
                    result += X;
                }
            }
        } catch (Exception e) {
            result = "The data in input have not been found in the databases.";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jl_title = new javax.swing.JLabel();
        jl_request = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jt_keywords = new javax.swing.JTextArea();
        jl_function = new javax.swing.JLabel();
        jb_hashsearch = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jt_results = new javax.swing.JTextArea();
        jt_errors = new javax.swing.JTextField();
        jb_reset = new javax.swing.JButton();
        jl_indication = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jl_title.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jl_title.setText("Program hash search");

        jl_request.setText("Enter the keywords :");

        jt_keywords.setColumns(20);
        jt_keywords.setRows(5);
        jScrollPane1.setViewportView(jt_keywords);

        jl_function.setText("Execute : ");

        jb_hashsearch.setText("hash search");
        jb_hashsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_hashsearchActionPerformed(evt);
            }
        });

        jt_results.setColumns(20);
        jt_results.setRows(5);
        jScrollPane2.setViewportView(jt_results);

        jb_reset.setText("reset");
        jb_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jb_resetActionPerformed(evt);
            }
        });

        jl_indication.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jl_indication.setText("Let a space between each keywords");

        jLabel1.setFont(new java.awt.Font("Tahoma", 3, 11)); // NOI18N
        jLabel1.setText("informations");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addComponent(jl_title))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jl_function)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jb_hashsearch))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jl_request)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jScrollPane1)
                                    .addComponent(jl_indication, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jt_errors, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jb_reset)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jl_title)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jl_request)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jl_indication)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jb_hashsearch)
                    .addComponent(jl_function))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jt_errors, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jb_reset))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jb_hashsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_hashsearchActionPerformed
        String allKeywords = jt_keywords.getText();
        if (allKeywords == null || allKeywords.equalsIgnoreCase("")) {
            jt_errors.setText("Enter keywords valid.");
        } else {
            List<String> keywords = new ArrayList();
            StringTokenizer st = new StringTokenizer(allKeywords);
            while (st.hasMoreElements()) {
                keywords.add(st.nextElement().toString());
            }
            try {
                String result = "the results are : \n";
                long start = System.nanoTime();
                result += hashSearch(keywords);
                long duree = (System.nanoTime() - start);
                result += "\n";
                result += "Time of this request is : " + duree + " nanoseconds \n or " + duree / 1000000 + " milliseconds \n or " + +duree / 1000000000 + " seconds." + "\n";
                jt_results.append(result);
            } catch (Exception ex) {
                Logger.getLogger(SearchEngineFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jb_hashsearchActionPerformed

    private void jb_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jb_resetActionPerformed
        jt_keywords.setText("");
        jt_results.setText("");
        jt_errors.setText("");
    }//GEN-LAST:event_jb_resetActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        JOptionPane.showConfirmDialog(
                    this,
                    "This program has been realized during my internship "
                + "in Nagano\n in Japan from a research called Hash search : an "
                + " efficient SLCA-based\nkeyword search algorithm on XML documents written by \n"
                 +" Weiyan Wang, Xiaoling Wang and Aoying Zhou. \n\n Angèle Demeurant.",
                    "Informations",
                    JOptionPane.CLOSED_OPTION);
      //  JOptionPane.showConfirmDialog(rootPane, create)
    }//GEN-LAST:event_jLabel1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SearchEngineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SearchEngineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SearchEngineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SearchEngineFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
         /*
         PREPARE THE DATABASE TO BE USED
         */
        accessToFrequencyTableDB();
        accessToDSPDB();
        accessToNLDB();
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SearchEngineFrame().setVisible(true);

            }
        });

        /* try {
           
         closeAllTheDatabases();
         } catch (DatabaseException ex) {
         Logger.getLogger(SearchEngineFrame.class.getName()).log(Level.SEVERE, null, ex);
         }*/
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jb_hashsearch;
    private javax.swing.JButton jb_reset;
    private javax.swing.JLabel jl_function;
    private javax.swing.JLabel jl_indication;
    private javax.swing.JLabel jl_request;
    private javax.swing.JLabel jl_title;
    private javax.swing.JTextField jt_errors;
    private javax.swing.JTextArea jt_keywords;
    private javax.swing.JTextArea jt_results;
    // End of variables declaration//GEN-END:variables
}

/* number of lines of this program :
AccessFTDB : 181
AccessDSPDB : 236
AccessNLDB : 221
Author : 77
Incollection : 235
Sets : 66
IndexBuilder : 728
Search Engine : 784
total : 2 528 lines
*/