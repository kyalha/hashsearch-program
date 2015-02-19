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
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * This class IndexBuilder has been created during my internhip in Japan,
 * where I have to create an Hash-search algorithm.
 *  * This class allows to access to the database Node list
 * @author angele
 */
public class AccessNLDB {

    private final int EXIT_SUCCESS = 0;
    private final int EXIT_FAILURE = 1;
    private final Database db;
    private final SortedMap map;
    private final Environment env;
    private final boolean create = true;

    /**
     * Constructor for the AccessExample object
     * @param env Description of the Parameter
     * @param databaseName
     * @exception Exception Description of the Exception
     */
    public AccessNLDB(Environment env, String databaseName)
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
            dbConfig.setType(DatabaseType.BTREE);
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
            System.err.println("accessNLDB: " + e.toString());
            System.exit(1);
        } catch (java.lang.Exception e) {
            System.err.println("accessNLDB: " + e.toString());
            System.exit(1);
        }

    }

    /**
     * Put data in the database
     * @param key
     * @param data 
     */
    public void putDataInDataBase(String key, String data) {
        map.put(key.getBytes(), data.getBytes());
    }

    /**
     * Read all the rows in the database
     */
    public void readAllInDataBase() {
        System.out.println("");
        Iterator iter = map.entrySet().iterator();
        System.out.println("Reading all the data of the NL ");
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            System.out.println("found \""
                    + new String((byte[]) entry.getKey())
                    + "\" key with data \""
                    + new String((byte[]) entry.getValue()) + "\"");
        }
    }
    
    /**
     * if the database contains the key
     * @param keyword
     * @return boolean
     */
    public boolean containsTheKey(String keyword){
        if(map.containsKey(keyword.getBytes())) {
            return true;
        }
        return false;
    }

    /**
     * Find values thanks to a key
     * @param key
     * @return String
     */
    public String  findValueThanksToKey(String key) {
        String value = "";
        Iterator iter = map.entrySet().iterator();
        System.out.println("Finding a data by the key: " + key);
        if (key == null || !map.containsKey(key.getBytes())) {
            value = ("The value searched doesn't exist");
        }
       
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (new String((byte[]) entry.getKey()).equalsIgnoreCase(key)) {
                value = new String((byte[]) entry.getValue());
            } 
        }
        return value;
    }

    /**
     * Remove all in the database
     */
    public void removeAllInDataBase() {
        System.out.println("");
        System.out.println("Removing all the data in the Database NodeList");
        map.clear();
    }

    /**
     * Remove one row in the database thanks to a key
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
     * Find a key thanks to a value
     * @param value
     * @return String
     */
    public String findTheKeyForAValue(String value) {
        String key = "";
        Iterator iter = map.entrySet().iterator();
        System.out.println("");
        System.out.println("Finding the key by the value: " + value);
        if (!map.containsValue(value.getBytes())) {
            key = ("The key searched doesn't exist");
        }
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (new String((byte[]) entry.getValue()).contains(value)) {
                key = new String((byte[]) entry.getKey());
            }
        }
        return key;
    }

    /**
     * Return the databse
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

