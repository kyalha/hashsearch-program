/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HashSearch.AccessDB;

import com.sleepycat.bind.ByteArrayBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.Environment;
import HashSearch.Entities.SetS;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * This class IndexBuilder has been created during my internhip in Japan, where
 * I have to create an Hash-search algorithm. This class allows to access to the
 * database DSP
 *
 * @author angele
 */
public class AccessDSPDB {

    private final int EXIT_SUCCESS = 0;
    private final int EXIT_FAILURE = 1;
    private final Database db;
    private final SortedMap map;
    private final Environment env;
    private final boolean create = true;

    /**
     * Constructor for the AccessExample object
     *
     * @param env Description of the Parameter
     * @param databaseName
     * @exception Exception Description of the Exception
     */
    public AccessDSPDB(Environment env, String databaseName)
            throws Exception {

        this.env = env;
        ByteArrayBinding keyBinding = new ByteArrayBinding();
        ByteArrayBinding dataBinding = new ByteArrayBinding();
        //
        // Open a data store.
        //
        DatabaseConfig dbConfig = new DatabaseConfig();
        if (create) {
            dbConfig.setAllowCreate(true);
            dbConfig.setType(DatabaseType.HASH);

        }
        this.db = env.openDatabase(null, databaseName, null, dbConfig);
        //
        // Now create a collection style map view of the data store
        // so that it is easy to work with the data in the database.
        //
        this.map = new StoredSortedMap(db, keyBinding, dataBinding, true);
    }

    /**
     * Close the database and environment.
     *
     * @throws com.sleepycat.db.DatabaseException
     */
    public void close()
            throws DatabaseException {
        db.close();
        env.close();
    }

    /**
     * Main processing method for the AccessExample object
     */
    public void run() {
        TransactionRunner tr = new TransactionRunner(env);
        try {
            tr.run(
                    new TransactionWorker() {
                        @Override
                        public void doWork() throws DatabaseException {
                        }
                    });
        } catch (com.sleepycat.db.DatabaseException e) {
            System.err.println("AccessDSPDB: " + e.toString());
            System.exit(1);
        } catch (java.lang.Exception e) {
            System.err.println("AccessDSPDB: " + e.toString());
            System.exit(1);
        }

    }

    /**
     * Put data in database
     *
     * @param key
     * @param data
     */
    public void putDataInDataBase(String key, int data) {
        SetS set = new SetS(key, data);
        String theSet = set.getKeyword() + "," + String.valueOf(set.getPreorderid());
        map.put(theSet.getBytes(), String.valueOf(1).getBytes());
    }

    /**
     * Read all the rows in database
     */
    public void readAllInDataBase() {
        System.out.println("");
        Iterator iter = map.entrySet().iterator();
        System.out.println("Reading all the data of the DSP ");
        while (iter.hasNext()) {
            SortedMap.Entry entry = (SortedMap.Entry) iter.next();
            System.out.println("found \""
                    + new String((byte[]) entry.getKey())
                    + "\" key with data \""
                    + new String((byte[]) entry.getValue()) + "\"");
        }
    }

    /**
     * find a value in the database thanks to the key
     *
     * @param key
     */
    public void findValueThanksToKey(SetS key) {
        System.out.println("");
        Iterator iter = map.entrySet().iterator();
        System.out.println("Finding a data with the key: " + key.getKeyword());
        if (key.getKeyword() == null) {
            System.out.println("Enter a valid key.");
        } else {
            if (key.getKeyword().equals("")) {
                System.out.println("Enter a valid key.");
            }

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String completeKeyword = new String((byte[]) entry.getKey());
                String exactKeyword = "";
                for (int i = 0; i < completeKeyword.length(); i++) {
                    exactKeyword = completeKeyword.substring(0, completeKeyword.indexOf(","));
                }
                if (exactKeyword.toLowerCase().equalsIgnoreCase(key.getKeyword().toLowerCase())) {
                    System.out.println("key : " + new String((byte[]) entry.getKey()) + " --- the values : " + new String((byte[]) entry.getValue()));
                } else {
                    System.out.println("Enter a valid key.");
                }
            }
        }
    }

    /**
     * Remove all the rows in the database
     */
    public void removeAllInDataBase() {
        System.out.println("");
        System.out.println("Removing all the data in the Database DSP ");
        map.clear();
    }

    /**
     * Remove one row thanks to a key
     *
     * @param key
     */
    public void removeOneRowThanksToAkey(String key) {
        System.out.println("");
        Iterator iter = map.entrySet().iterator();
        System.out.println("Removing data by the key: " + key);
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (new String((byte[]) entry.getKey()).equalsIgnoreCase(key)) {
                map.remove(entry.getKey());
            }

        }
    }

    /**
     * Verify if the database contains the set
     *
     * @param key
     * @return boolean
     */
    public boolean verifyIfDBcontainsTheKeyAndValue(SetS key) {

        Iterator iter = map.entrySet().iterator();
        boolean contains = false;
        String mKeyNumber = "";
        String mKeyword = "";
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            // keyword,id
            String completeID = new String((byte[]) entry.getKey());
            for (int i = 0; i < completeID.length(); i++) {
                mKeyword = completeID.substring(0, completeID.indexOf(","));
                mKeyNumber = completeID.substring(completeID.indexOf(",") + 1, completeID.length());
            }
            // System.out.println(mKeyword + " : " + mKeyNumber);
            if (mKeyword.equalsIgnoreCase(key.getKeyword().toLowerCase())
                    && mKeyNumber.equals(String.valueOf(key.getPreorderid()))) {
                contains = true;

            }

        }
        return contains;
    }

    /**
     * Return the database
     *
     * @return Database
     */
    public Database getDatabase() {
        return this.db;
    }

    /**
     * Return the size of the map
     * @return int
     */
    public int getSizeOfMap() {
        return this.map.size();
    }
}
