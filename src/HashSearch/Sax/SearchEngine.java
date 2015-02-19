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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This class IndexBuilder has been created during my internhip in Japan, where
 * I have to create an Hash-search algorithm. This class SearchEngine use the
 * different databases
 *
 * @author angele
 */
public class SearchEngine {

    private static AccessFTDB accessFTDB;
    private static AccessDSPDB accessDSPDB;
    private static AccessNLDB accessNLDB;

    private static boolean create = true;
    private static String rapport = "";
    private static String rapportTestLimitedSLCA = "";
    private static String rapportTestHashSearch = "";
    private static long start;
    private static long duree;

    /**
     * Method main
     *
     * @param args
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws FileNotFoundException
     * @throws DatabaseException
     * @throws Exception
     */
    public static void main(String[] args) throws IOException, SAXException,
            ParserConfigurationException, FileNotFoundException, DatabaseException, Exception {
        long startProgram = System.currentTimeMillis();
        /*
         PREPARE THE DATABASE TO BE USED
         */
        accessToFrequencyTableDB();
        accessToDSPDB();
        accessToNLDB();

        /*
         Read all the keywords in the file in order to fill the hasmap
         */
        /*
         TESTS
         */
        // System.out.println(accessNLDB.findValueThanksToKey("William"));
        //testForLimitedSLCA();
        // testForlimitedSLCA_versionWhithoutBinarySearch();
        // readAllTheDatabases();
        //  testWithDatabaseDSP();
        //   testWithDatabaseFT();
        //  testWithDatabaseNL();
        testForHashSearch();
        // othersTestsForLimitedSLCA();
        //othersTestsForHashSearch();
        /*
         - Close all the databases and display the execution time
         - EVALUATE THE TIME OF EXECUTION OF THIS PROGRAMM
         */
        //accessFTDB.readAllInDataBase();
        closeAllTheDatabases();
        //writeInFile("rapport_test_limitedLSCA", rapportTestLimitedSLCA);
        writeInFile("rapport_test_Hashsearch", rapportTestHashSearch);
        duree = (System.currentTimeMillis() - startProgram);
        System.out.println("Total execution time : " + duree + " milliseconds." + "\n");
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
    public static SetS getTheLowestFrequency(List<String> keywords) {
        // System.out.println("--- Test for getTheLowestFrequency ");
        int frequency = 0;
        SetS set = new SetS();
        frequency = Integer.parseInt(accessFTDB.findValueThanksToKey(keywords.get(frequency)));
        for (String keyword : keywords) {
            if (Integer.parseInt(accessFTDB.findValueThanksToKey(keyword)) <= frequency) {
                frequency = Integer.parseInt(accessFTDB.findValueThanksToKey(keyword));
                set = new SetS(keyword, frequency);
            }
        }

        return set;
    }

    /**
     * This method extracts the digits from a String composed of nodes and
     * digits
     *
     * @param preordersID
     * @return List<Integer>
     */
    public static List<Integer> extractDigitsFromPreorderID(String preordersID) {
        /*
         We separate the string in many little strings
         and we select only the words or numbers
         */
        List<Integer> digits = new ArrayList();
        Pattern p = Pattern.compile("\\W");
        String[] items = p.split(preordersID, 100);
        for (String item : items) {
            if (!item.equals("")) {
                digits.add(Integer.parseInt(item));
            }
        }
        return digits;
    }

    /**
     * Extract from a list of path of nodes, each path of the node
     *
     * @param nodeList
     * @return
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
    public static String limitedSLCA(int anc, List<String> keywords, String preordersID) {
        if (anc == 0 || keywords == null || keywords.isEmpty()
                || preordersID == null || preordersID.equalsIgnoreCase("")) {
            return "Entrer dans des valeurs valides pour"
                    + " effectuer le limitedSLCA.";
        }
        System.out.println("Limited SLCA -- test for : " + preordersID);
        String limitedslca = "";
        String result = "";
        boolean LCA = true;
        List<Integer> idArray = extractDigitsFromPreorderID(preordersID);
        Collections.sort(idArray);
        int lastPosition = 0;
        for (int i = 0; i < idArray.size(); i++) {
            lastPosition = i;
        }

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
            limitedslca = String.valueOf(idArray.get(left - 1));
        }
        for (int i = 0; i < limitedslca.length(); i++) {
            result = preordersID.substring(0, preordersID.indexOf(limitedslca) + limitedslca.length()) + "]";
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
    public static String limitedSLCA_versionWhithoutBinarySearch(int anc, List<String> keywords, String preordersID) {
        System.out.println("Limited limitedSLCA_versionWhithoutBinarySearch -- test for : " + preordersID);
        String result = "";
        List<Integer> idArray = extractDigitsFromPreorderID(preordersID);
        Collections.sort(idArray);
        boolean isOK = true;
        int theID = 0;
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
                result = mID.group().substring(0, theID + 1) + "]";

            } else {
                result = mID.group() + "]";
            }
        }
        return result;
    }

    /**
     * This method is the hash-search. Thanks to a keyword, hash-search look for
     * the limited slca oh the keyword having the lowest frequency and return
     * finally the slca's of this keywords
     *
     * @param keywords
     * @return String
     * @throws java.lang.Exception
     */
    public static String hashSearch(List<String> keywords) throws Exception {
        System.out.println("Hash search : ");

        String result = "";

        try {
            if (keywords == null) {
                return "The keywords in input are not correct.";
            }
            SetS f1 = new SetS();
            f1 = getTheLowestFrequency(keywords);
            String NLOfkeyword = accessNLDB.findValueThanksToKey(f1.getKeyword());
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
        } catch (NumberFormatException e) {
            result = "The data in input have not been found in the databases.";
        }
        return result;
    }

    /**
     * Method for test the LIMITEDSLCA which contains 3 examples
     */
    public static void testForLimitedSLCA() {
        long timeComplete = 0;
        List<String> keywordsToTest = new ArrayList();
        String slca = "";
        keywordsToTest.clear();
        keywordsToTest.add("Carlson");
        keywordsToTest.add("Amelia");
        String value = "[1.2.9.10]";
        int index = 0;
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            slca = limitedSLCA(1, keywordsToTest, value);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        slca = limitedSLCA(1, keywordsToTest, value);
        duree = (System.currentTimeMillis() - start);
        System.out.println("slca : " + slca);
        rapportTestLimitedSLCA += ("\n");
        rapportTestLimitedSLCA += ("testForLimitedSLCA :  \n");
        rapportTestLimitedSLCA += ("Test with 2 keywords : \n");
        rapportTestLimitedSLCA += ("Carlson with preordersID : [1.2.9.11] \n");
        rapportTestLimitedSLCA += ("Amelia with preordersID : [1.2.9.10] \n");
        rapportTestLimitedSLCA += ("the node : Amelia" + " and preordersID : [1.2.9.10]\n");
        rapportTestLimitedSLCA += ("results of the test LimitedSLCA : " + slca + "\n");
        long average = timeComplete / index;
        rapportTestLimitedSLCA += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        keywordsToTest.clear();
        keywordsToTest.add("Carlson");
        keywordsToTest.add("Amelia");
        keywordsToTest.add("Daniel");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            slca = limitedSLCA(1, keywordsToTest, value);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        slca = limitedSLCA(1, keywordsToTest, value);
        duree = (System.currentTimeMillis() - start);
        rapportTestLimitedSLCA += ("\n");
        rapportTestLimitedSLCA += ("testForLimitedSLCA :  \n");
        rapportTestLimitedSLCA += ("Test with 3 keywords : \n");
        rapportTestLimitedSLCA += ("Carlson with preordersID : [1.2.9.11] \n");
        rapportTestLimitedSLCA += ("Amelia with preordersID : [1.2.9.10] \n");
        rapportTestLimitedSLCA += ("Daniel with preordersID : [1.2.15.16] \n");
        rapportTestLimitedSLCA += ("the node : Amelia" + " and preordersID : [1.2.9.10]\n");
        rapportTestLimitedSLCA += ("results of the test LimitedSLCA : " + slca + "\n");
        average = timeComplete / index;
        rapportTestLimitedSLCA += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        keywordsToTest.clear();
        keywordsToTest.add("Carlson");
        keywordsToTest.add("Amelia");
        keywordsToTest.add("Daniel");
        keywordsToTest.add("William");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            slca = limitedSLCA(1, keywordsToTest, value);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        slca = limitedSLCA(1, keywordsToTest, value);
        duree = (System.currentTimeMillis() - start);
        rapportTestLimitedSLCA += ("\n");
        rapportTestLimitedSLCA += ("testForLimitedSLCA :  \n");
        rapportTestLimitedSLCA += ("Test with 4 keywords : \n");
        rapportTestLimitedSLCA += ("Carlson with preordersID : [1.2.9.11] \n");
        rapportTestLimitedSLCA += ("Amelia with preordersID : [1.2.9.10] \n");
        rapportTestLimitedSLCA += ("Daniel with preordersID : [1.2.15.16] \n");
        rapportTestLimitedSLCA += ("William with preordersID : [1.2.18.19] \n");
        rapportTestLimitedSLCA += ("the node : Amelia" + " and preordersID : [1.2.9.10]\n");
        rapportTestLimitedSLCA += ("results of the test LimitedSLCA : " + slca + "\n");
        average = timeComplete / index;
        rapportTestLimitedSLCA += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

    }

    /**
     * Method for test the tlimitedSLCA_versionWhithoutBinarySearch which
     * contains 3 examples
     */
    public static void testForlimitedSLCA_versionWhithoutBinarySearch() {

        System.out.println("");
        System.out.println("testForLimitedSLCA2 : ");
        List<String> keywordsToTest = new ArrayList();
        System.out.println("Test with : ");
        System.out.println("Carlson with preordersID : [1.2.9.11]");
        System.out.println("Amelia with preordersID : [1.2.9.10]");
        keywordsToTest.add("Carlson");
        keywordsToTest.add("Amelia");
        System.out.println("the node : Amelia" + " and preordersID : [1.2.9.10]");
        String value = "[1.2.9.10]";
        String lslca = limitedSLCA_versionWhithoutBinarySearch(1, keywordsToTest, value);
        System.out.println("results of the test LimitedSLCA : " + lslca);

        System.out.println("");
        System.out.println("Test with : ");
        System.out.println("Ahad with preordersID : [1.2.6.8]");
        System.out.println("Rafiul with preordersID : [1.2.6.7]");
        keywordsToTest.clear();
        keywordsToTest.add("Ahad");
        keywordsToTest.add("Rafiul");
        System.out.println("the node : Amelia" + " and preordersID : [1.2.9.10]");
        String value2 = "[1.2.9.10]";
        String lslca2 = limitedSLCA_versionWhithoutBinarySearch(1, keywordsToTest, value2);
        System.out.println("results of the test LimitedSLCA : " + lslca2);

        System.out.println("");
        System.out.println("Test with : ");
        System.out.println("José with preordersID : [1.49.50.51]");
        System.out.println("Rafiul with preordersID : [1.2.6.7]");
        keywordsToTest.clear();
        keywordsToTest.add("José");
        keywordsToTest.add("Rafiul");
        System.out.println("the node : Amelia" + " and preordersID : [1.2.9.10]");
        String value3 = "[1.2.9.10]";
        String lslca3 = limitedSLCA_versionWhithoutBinarySearch(1, keywordsToTest, value3);
        System.out.println("results of the test LimitedSLCA : " + lslca3);
    }

    /**
     * This function test the hash-search thanks to 3 examples
     *
     * @throws Exception
     */
    public static void testForHashSearch() throws Exception {
        long timeComplete = 0;
        int index = 0;
        List<String> keywordsToTest = new ArrayList();
        keywordsToTest.clear();
        keywordsToTest.add("Jurgen");
        keywordsToTest.add("Rafiul");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        long average = timeComplete / index;
        // =========================== TEST FOR A FREQUENCY OF ONE ================================
        rapportTestHashSearch += ("\n");
        System.out.println("=========================== TEST FOR A FREQUENCY OF ONE ================================");
        rapportTestHashSearch += ("Test with 2 keywords with a frequency of 1 : Jurgen and Rafiul \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");

        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        timeComplete = 0;
        keywordsToTest.clear();
        keywordsToTest.add("José");
        keywordsToTest.add("Blakeley");
        keywordsToTest.add("Extending");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        average = timeComplete / index;
        rapportTestHashSearch += ("\n");
        rapportTestHashSearch += ("Test with 3 keywords with a frequency of 1 : José and Blakeley and Extending \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");
        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        timeComplete = 0;
        keywordsToTest.clear();
        keywordsToTest.add("Breitbart");
        keywordsToTest.add("Yuri");
        keywordsToTest.add("Management");
        keywordsToTest.add("Silberschatz");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        average = timeComplete / index;
        rapportTestHashSearch += ("\n");
        rapportTestHashSearch += ("Test with 4 keywords with a frequency of 1 : Breitbart and Yuri and Management and Silberschatz \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");
        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        // =========================== TEST FOR A FREQUENCY OF TEN ================================
        timeComplete = 0;
        keywordsToTest.clear();
        keywordsToTest.add("html");
        keywordsToTest.add("kim95");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        average = timeComplete / index;
        rapportTestHashSearch += ("\n");
        System.out.println("=========================== TEST FOR A FREQUENCY OF TEN  ================================");
        rapportTestHashSearch += ("Test with 2 keywords with a frequency of 10 : html and kim95 \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");
        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        timeComplete = 0;
        keywordsToTest.clear();
        keywordsToTest.add("kim95");
        keywordsToTest.add("collections");
        keywordsToTest.add("Modern");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        average = timeComplete / index;
        rapportTestHashSearch += ("\n");
        rapportTestHashSearch += ("Test with 3 keywords with a frequency of 10 : kim95 and collections and Modern \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");
        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

        timeComplete = 0;
        keywordsToTest.clear();
        keywordsToTest.add("1995");
        keywordsToTest.add("db");
        keywordsToTest.add("Modern");
        keywordsToTest.add("kim95");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        average = timeComplete / index;
        rapportTestHashSearch += ("\n");
        rapportTestHashSearch += ("Test with 4 keywords with a frequency of 10 : 1995 and db and Modern and kim95 \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");
        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";
        // =========================== TEST FOR A FREQUENCY MORE THAN  TEN ================================
        rapportTestHashSearch += ("\n");
        System.out.println("=========================== TEST FOR A FREQUENCY MORE THAN TEN  ================================");
        timeComplete = 0;
        keywordsToTest.clear();
        keywordsToTest.add("Systems");
        keywordsToTest.add("Database");
        for (index = 0; index < 100; index++) {
            start = System.currentTimeMillis();
            hashSearch(keywordsToTest);
            duree = (System.currentTimeMillis() - start);
            timeComplete += duree;
        }
        average = timeComplete / index;
        rapportTestHashSearch += ("\n");
        rapportTestHashSearch += ("Test with 2 keywords with a frequency more than ten : Systems and Database  \n");
        for (String keyword : keywordsToTest) {
            rapportTestHashSearch += "frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n";
        }
        rapportTestHashSearch += ("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");

        rapportTestHashSearch += "Average time of this execution for " + index + " times : " + average + " milliseconds." + "\n";

    }

    public static void othersTestsForLimitedSLCA() {
        List<String> keywordsToTest = new ArrayList();
        String slca = "";
        keywordsToTest.clear();
        keywordsToTest.add("Rafiul");
        keywordsToTest.add("Ahad");
        String value = "[1.2.9.10]";
        slca = limitedSLCA(1, keywordsToTest, value);
        System.out.println("slca : " + slca);
        System.out.println("\n");
        System.out.println("testForLimitedSLCA :  \n");
        System.out.println("Test with 2 keywords : \n");
        System.out.println("Rafiul with preordersID : [1.2.6.7] \n");
        System.out.println("Ahad with preordersID : [1.2.6.8] \n");
        System.out.println("the node : Amelia" + " and preordersID : [1.2.9.10]\n");
        System.out.println("results of the test LimitedSLCA : " + slca + "\n");
    }

    public static void othersTestsForHashSearch() throws Exception {
        List<String> keywordsToTest = new ArrayList();
        String slca = "";
        keywordsToTest.clear();
        keywordsToTest.add("html");
        keywordsToTest.add("kim95");

        System.out.println("Test with 2 keywords with a frequency of 10 : html and kim95 \n");
        for (String keyword : keywordsToTest) {
            System.out.println("frequency of the keyword : " + keyword + " is : " + accessFTDB.findValueThanksToKey(keyword) + "\n");
            System.out.println("NL of the keyword : " + keyword + " is : " + accessNLDB.findValueThanksToKey(keyword));
        }
        System.out.println("Results of testForHashSearch, the SLCAs are : " + hashSearch(keywordsToTest) + "\n");
    }

}
