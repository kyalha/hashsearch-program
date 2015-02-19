package HashSearch.Sax;

import HashSearch.AccessDB.AccessFTDB;
import HashSearch.AccessDB.AccessDSPDB;
import HashSearch.AccessDB.AccessNLDB;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.Environment;
import com.sleepycat.db.EnvironmentConfig;
import HashSearch.Entities.Author;
import HashSearch.Entities.InCollection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class IndexBuilder has been created during my internhip in Japan, where I
 have to create an Hash-search algorithm. This class allows to parse a XML
 * file which has a type of DBLP. By the parsing, we fill the three Databases
 * FrequencyTable, NodeList and DominationSetPool
 *
 * @author angele
 */
public class IndexBuilder extends DefaultHandler {

    private InCollection collection;
    private String temp;
    private final ArrayList<InCollection> collections = new ArrayList<>();
    private int indexCollect = 0;

    private static final List<String> allTheLines = new ArrayList();
    private static final List<String> allTheKeyWords = new ArrayList();
    private static final Map<String, Integer> FTSorted = new TreeMap<>();
    private static final Map<String, Integer> DSPSorted = new TreeMap<>();
    private static final Map<String, String> NLSorted = new TreeMap<>();

    private static AccessFTDB accessFTDB;
    private static AccessDSPDB accessDSPDB;
    private static AccessNLDB accessNLDB;
    private static final boolean create = true;

    private static String rapport = "";

    /**
     * The main method sets things up for parsing
     *
     * @param args
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws java.io.FileNotFoundException
     * @throws com.sleepycat.db.DatabaseException
     */
    public static void main(String[] args) throws IOException, SAXException,
            ParserConfigurationException, FileNotFoundException, DatabaseException {

        /*
         use the parser factory to create a SAXParser object
         Create a "parser factory" for creating SAX parsers
         Create an instance of this class; it defines all the handler methods
         */
        SAXParserFactory spfac = SAXParserFactory.newInstance();
        SAXParser sp = spfac.newSAXParser();
        IndexBuilder handler = new IndexBuilder();

        /*
         PREPARE THE DATABASE TO BE USED
         */
        accessToFrequencyTableDB();
        accessToDSPDB();
        accessToNLDB();

        //Finally, tell the parser to parse the input and notify the handler
      //  sp.parse("files/dblp_c.xml", handler);

        long start;
        long duree;
        start = System.currentTimeMillis();
      //  rapport += "Test : sax Parser Entity " + "\n";

        /*
         Read all the keywords in the file in order to fill the hasmap
         */
        
     //  readAllTheDatabases();
        /*
         - Close all the databases and display the execution time
         - EVALUATE THE TIME OF EXECUTION OF THIS PROGRAMM
         */
        closeAllTheDatabases();
        duree = (System.currentTimeMillis() - start);
        rapport += "\n";
        rapport += "Total execution time : " + duree + " milliseconds." + "\n";
        System.out.println("Total execution time : " + duree + " milliseconds." + "\n");

        // writeInFile("rapport1", rapport);
    }

    /**
     * This method is called when we wtart parsing the document
     */
    @Override
    public void startDocument() {
        System.out.println("Begin of the parsing. ");

    }

    /**
     * This method is called when the document has been parsed entirely
     */
    @Override
    public void endDocument() {
        System.out.println("End of the parsing");
    }

    /**
     *
     * When the parser encounters plain text (not XML elements), it calls this
     * method, which accumulates them in a string buffer
     * @param buffer
     * @param start
     * @param length
     */
    @Override
    public void characters(char[] buffer, int start, int length) {
        temp = new String(buffer, start, length);
        allTheLines.add(temp);

    }

    /**
     * * Every time the parser encounters the beginning of a new element, it
     * calls this method, which resets the string buffer
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes) throws SAXException {
        temp = "";
        indexCollect++;
        if (qName.equalsIgnoreCase("incollection")) {
            collection = new InCollection(indexCollect);
        }
    }

    /**
     *
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException When the parser encounters the end of an element, it
     * calls this method
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (qName.equalsIgnoreCase("incollection")) {
            collections.add(collection);
        } else if (qName.equalsIgnoreCase("author")) {
            collection.getListAuthors().add(new Author(temp, indexCollect));
            List<String> keywords = extractWordsFromLines(temp);
            for (String keyword : keywords) {
                indexCollect++;
            }
        } else if (qName.equalsIgnoreCase("title")) {
            collection.setTitle(temp);
            collection.setIdTitle(indexCollect);
            List<String> keywords = extractWordsFromLines(temp);
            for (String keyword : keywords) {
                indexCollect++;
            }
        } else if (qName.equalsIgnoreCase("pages")) {
            collection.setPages(temp);
            collection.setIdPages(indexCollect);
            List<String> keywords = extractWordsFromLines(temp);
            for (String keyword : keywords) {
                indexCollect++;
            }
        } else if (qName.equalsIgnoreCase("year")) {
            collection.setYear(Integer.parseInt(temp));
            collection.setIdYear(indexCollect);
            List<String> keywords = extractWordsFromLines(temp);
            for (String keyword : keywords) {
                indexCollect++;
            }
        } else if (qName.equalsIgnoreCase("booktitle")) {
            collection.setBooktitle(temp);
            collection.setIdBooktitle(indexCollect);
            List<String> keywords = extractWordsFromLines(temp);
            for (String keyword : keywords) {
                indexCollect++;
            }
        } else if (qName.equalsIgnoreCase("url")) {
            collection.setUrl(temp);
            collection.setIdUrl(indexCollect);
            List<String> keywords = extractWordsFromLines(temp);
            for (String keyword : keywords) {
                indexCollect++;
            }
        }
    }

    /**
     * @throws FileNotFoundException
     * @throws DatabaseException
     * @throws UnsupportedEncodingException This method create and add the
     * attributes of the object Collection, Besides, it's here that the map for
     * the NodeList and FrequencyTable are fill, and the Database DSP is fill
     */
    private void readList() throws FileNotFoundException, DatabaseException, UnsupportedEncodingException {
        System.out.println("Size of Collection " + collections.size());
        System.out.println("");
        int idKeyword = 0;
        for (int i = 0; i < collections.size(); i++) {
            /*
             FILL THE TABLE FT, DSP, NL ACCORDING TO THE AUTHOR
             */
            List<Author> authors = collections.get(i).getListAuthors();
            for (Author author : authors) {
                List<String> keywordsAuthors = extractWordsFromLines(author.getNom());
                idKeyword = author.getIdAuthor();
                for (String keyword : keywordsAuthors) {
                    /*
                     FILL THE HASHMAP
                     */
                    fillFT(keyword);
                    idKeyword++;
                    String preordersId = "";
                    preordersId = (("[" + "1" + "." + collections.get(i).getIdInCollect()
                            + "." + author.getIdAuthor() + "." + idKeyword + "]"));
                    fillNL(keyword, preordersId);
                    /*
                     FILL THE DATABASE
                     */
                     accessDSPDB.putDataInDataBase(keyword, 1);
                     accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdInCollect());
                     accessDSPDB.putDataInDataBase(keyword, author.getIdAuthor());
                     accessDSPDB.putDataInDataBase(keyword, idKeyword);
                }
            }
            /*
             FILL THE TABLE FT, DSP, NL ACCORDING TO THE TITLE
             */
            List<String> keywordsTitles = extractWordsFromLines(collections.get(i).getTitle());
            idKeyword = collections.get(i).getIdTitle();
            for (String keyword : keywordsTitles) {
                idKeyword++;
                fillFT(keyword);
                String preordersId = "";
                preordersId = (("[" + "1" + "." + collections.get(i).getIdInCollect()
                        + "." + collections.get(i).getIdTitle() + "." + idKeyword + "]"));
                fillNL(keyword, preordersId);
              
                 accessDSPDB.putDataInDataBase(keyword, 1);
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdTitle());
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdInCollect());
                 accessDSPDB.putDataInDataBase(keyword, idKeyword);
                   /*
                 FILL THE DATABASE
                 */
                // accessFTDB.putDataInDataBase(keyword);*/
            }
            /*
             FILL THE TABLE FT, DSP, NL ACCORDING TO THE PAGES
             */
            List<String> keywordsPages = extractWordsFromLines(collections.get(i).getPages());
            idKeyword = collections.get(i).getIdPages();
            for (String keyword : keywordsPages) {
                idKeyword++;
                fillFT(keyword);
                String preordersId = "";
                preordersId = (("[" + "1" + "." + collections.get(i).getIdInCollect()
                        + "." + collections.get(i).getIdPages() + "." + idKeyword + "]"));
                fillNL(keyword, preordersId);
                /*
                 FILL THE DATABASE
                 */
                /* accessDSPDB.putDataInDataBase(keyword, 1);
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdPages());
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdInCollect());
                 accessDSPDB.putDataInDataBase(keyword, idKeyword);*/
                // accessFTDB.putDataInDataBase(keyword);*/
            }
            /*
             FILL THE TABLE FT, DSP, NL ACCORDING TO THE YEAR
             */
            List<String> keywordsYear = extractWordsFromLines(String.valueOf(collections.get(i).getYear()));
            idKeyword = collections.get(i).getIdYear();
            for (String keyword : keywordsYear) {
                idKeyword++;
                fillFT(keyword);
                String preordersId = "";
                preordersId = (("[" + "1" + "." + collections.get(i).getIdInCollect()
                        + "." + collections.get(i).getIdYear() + "." + idKeyword + "]"));
                fillNL(keyword, preordersId);
                /*
                 FILL THE DATABASE
                 */
                /*  accessDSPDB.putDataInDataBase(keyword, 1);
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdYear());
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdInCollect());
                 accessDSPDB.putDataInDataBase(keyword, idKeyword);*/
                // accessFTDB.putDataInDataBase(keyword);
            }
            /*
             FILL THE TABLE FT, DSP, NL ACCORDING TO THE BOOKTITLE
             */
            List<String> keywordsBooktitle = extractWordsFromLines((collections.get(i).getBooktitle()));
            idKeyword = collections.get(i).getIdBooktitle();
            for (String keyword : keywordsBooktitle) {
                idKeyword++;
                fillFT(keyword);
                String preordersId = "";
                preordersId = (("[" + "1" + "." + collections.get(i).getIdInCollect()
                        + "." + collections.get(i).getIdBooktitle() + "." + idKeyword + "]"));
                fillNL(keyword, preordersId);
                /*
                 FILL THE DATABASE
                 */
                /*  accessDSPDB.putDataInDataBase(keyword, 1);
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdBooktitle());
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdInCollect());
                 accessDSPDB.putDataInDataBase(keyword, idKeyword);*/
                // accessFTDB.putDataInDataBase(keyword);
            }
            /*
             FILL THE TABLE FT, DSP, NL ACCORDING TO THE URL
             */
            List<String> keywordsUrl = extractWordsFromLines((collections.get(i).getUrl()));
            idKeyword = collections.get(i).getIdUrl();
            for (String keyword : keywordsUrl) {
                idKeyword++;
                fillFT(keyword);
                String preordersId = "";
                preordersId = (("[" + "1" + "." + collections.get(i).getIdInCollect()
                        + "." + collections.get(i).getIdUrl() + "." + idKeyword + "]"));
                fillNL(keyword, preordersId);
                /*
                 FILL THE DATABASE
                 */
                /* accessDSPDB.putDataInDataBase(keyword, 1);
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdUrl());
                 accessDSPDB.putDataInDataBase(keyword, collections.get(i).getIdInCollect());
                 accessDSPDB.putDataInDataBase(keyword, idKeyword);*/
                //  accessFTDB.putDataInDataBase(keyword);
            }

        }
    }

    /**
     * Method wich fill the hashmap frequency table
     *
     * @param theKeyWord This method fill the map FrequencyTable
     */
    private static void fillFT(String theKeyWord) {
        if (FTSorted.containsKey(theKeyWord)) {
            FTSorted.put(theKeyWord, FTSorted.get(theKeyWord) + 1);
        } else {
            FTSorted.put(theKeyWord, 1);
        }
    }

    /**
     * This method read the map FrequencyTable
     */
    private static void readFT() {
        System.out.println("");
        System.out.println("read FT : ");
        rapport += "\n";
        rapport += " read FT : " + "\n";
        Iterator<String> keySetIteratorFT = FTSorted.keySet().iterator();
        while (keySetIteratorFT.hasNext()) {
            String key = keySetIteratorFT.next();
            System.out.println("key: " + key + " - " + " value: " + FTSorted.get(key));
            rapport += "key: " + key + " - " + " value: " + FTSorted.get(key) + "\n";

        }
    }

    /**
     * This method fill the Database of FrequencyTable We fill the database
     * thanks to the map We need to do it just once.
     */
    private static void fillTheDBFT() {
        Iterator<String> keySetIteratorFT = FTSorted.keySet().iterator();
        while (keySetIteratorFT.hasNext()) {
            String key = keySetIteratorFT.next();
            accessFTDB.putDataInDataBase(key, FTSorted.get(key));

        }
    }

    /**
     * @param preorderID
     * @param keyword This method fill the map DSP It was just for trying, not
     * usefull here
     */
    private static void fillDSP(int preorderID, String keyword) {
        DSPSorted.put(keyword, preorderID);
    }

    /**
     * This method read the map DSP only
     */
    private static void readDSP() {
        System.out.println("");
        System.out.println("read DSP : ");
        rapport += "\n";
        rapport += " read DSP : " + "\n";
        Iterator<String> keySetIteratorDSP = DSPSorted.keySet().iterator();
        while (keySetIteratorDSP.hasNext()) {
            String key = keySetIteratorDSP.next();
            System.out.println("key: " + key + " - " + " value: " + DSPSorted.get(key));
            rapport += "key: " + key + " - " + " value: " + DSPSorted.get(key) + "\n";
        }
    }

    /**
     * @param keyword
     * @param preordersId This method read the map NodeList
     */
    private static void fillNL(String keyword, String preordersId) {

        if (NLSorted.containsKey(keyword)) {
            NLSorted.put(keyword, NLSorted.get(keyword) + "," + preordersId);
        } else {
            NLSorted.put(keyword, preordersId);
        }
    }

    /**
     * This method read the map NodeList and fill the database NodeList We fill
     * the database thanks to the map We need to do it just once.
     */
    private static void readNL() {
        System.out.println("");
        System.out.println("read NL : ");
        rapport += "\n";
        rapport += " read NL : " + "\n";
        Iterator<String> keySetIteratorDSP = NLSorted.keySet().iterator();
        while (keySetIteratorDSP.hasNext()) {
            String key = keySetIteratorDSP.next();
            System.out.println("key: " + key + " - " + " value: " + NLSorted.get(key));
            accessNLDB.putDataInDataBase(key, NLSorted.get(key));
            rapport += "key: " + key + " - " + " value: " + NLSorted.get(key) + "\n";
        }
    }

    /**
     * This method fill the database NodeList according to the map which is
     * already filled
     */
    private static void fillTheDBNL() {
        System.out.println("");
        Iterator<String> keySetIteratorDSP = NLSorted.keySet().iterator();
        while (keySetIteratorDSP.hasNext()) {
            String key = keySetIteratorDSP.next();
            accessNLDB.putDataInDataBase(key, NLSorted.get(key));
        }
    }

    /**
     * This method extract each word of a string which has at leat 2 letters and
     * return the list of the words extracted
     *
     * @param s
     * @return List<String>
     */
    public static List<String> extractWordsFromLines(String s) {
        //first letter of the word
        // if firstChar = -1 than the word is not read
        int firstChar = -1;
        //length of the word
        int length = 0;
        // for each caracter
        allTheKeyWords.clear();
        for (int i = 0; i < s.length(); i++) {
            //(Character.isSpaceChar(s.charAt(i)) || Character.isJavaIdentifierPart(s.charAt(i)))

            if (!Character.isLetterOrDigit(s.charAt(i))) {
                if (firstChar != -1) {
                    //  System.out.println(s.substring(firstChar, firstChar + length));
                    if (s.substring(firstChar, firstChar + length).length() > 1) {
                        String keyword = s.substring(firstChar, firstChar + length);
                        allTheKeyWords.add(keyword);
                    }
                }
                firstChar = -1;
                length = 0;
            } else {
                if (firstChar == -1) {
                    firstChar = i;
                }
                length++;
            }
            if (firstChar != -1 && i == s.length() - 1) {
                if (s.substring(firstChar, firstChar + length).length() > 1) {
                    String keyword = s.substring(firstChar, firstChar + length);
                    allTheKeyWords.add(keyword);
                }
            }
        }
        return allTheKeyWords;
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
     * DISPLAY THE SIZE OF THE DATABASES
     */
    public static void displayTheSizeOfDatabases() {
        System.out.println("");
        System.out.println("Size of FT : " + accessFTDB.getSizeOfMap());
        System.out.println("Size of DSP : " + accessDSPDB.getSizeOfMap());
        System.out.println("Size of NL : " + accessNLDB.getSizeOfMap());
    }

    /**
     * Read the dabases
     */
    public static void readAllTheDatabases() {
        accessFTDB.readAllInDataBase();
        accessDSPDB.readAllInDataBase();
        accessNLDB.readAllInDataBase();

    }

    /**
     * This method remove all the data in Databases FT and NL and refill this
     * databases thanks to the hashmap
     */
    public static void refillDatabases() {
        accessDSPDB.removeAllInDataBase();
        accessNLDB.removeAllInDataBase();
        fillTheDBFT();
        fillTheDBNL();

    }

    /**
     * This method extracts the different path of nodes of a String
     * @param nodeList
     * @return List<String
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


}
