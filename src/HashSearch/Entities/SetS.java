/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HashSearch.Entities;

/**
 * This class IndexBuilder has been created during my internhip in Japan, where
 * I have to create an Hash-search algorithm.
 * This class reprents a set necessary for put data in databse
 * @author angele
 */
public class SetS{

    private String keyword;
    private Integer preorderid;

    /**
     * Constructor
     * @param keyword
     * @param preorderid 
     */
    public SetS(String keyword, Integer preorderid) {
        this.keyword = keyword;
        this.preorderid = preorderid;
    }

    /**
     * Constructor empty
     */
    public SetS() {

    }

    /**
     * @return String
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param keyword 
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    /**
     * @return Integer
     */
    public Integer getPreorderid() {
        return preorderid;
    }

    /**
     * @param preorderid 
     */
    public void setPreorderid(Integer preorderid) {
        this.preorderid = preorderid;
    }
}
