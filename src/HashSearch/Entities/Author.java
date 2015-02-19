/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package HashSearch.Entities;

import java.util.List;

/**
 * This class IndexBuilder has been created during my internhip in Japan, where I
 * have to create an Hash-search algorithm.
 * This class reprents the object author
 * @author angele
 */
public class Author {
    private String nom;
    private int idAuthor;
    private List<String> keywordsAuthor;
  
/**
 * Constructor
 * @param nom
 * @param idAuthor 
 */
    public Author(String nom, int idAuthor) {
        this.nom = nom;
        this.idAuthor = idAuthor;
    }

    /**
     * 
     * @return String
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom 
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return int
     */
    public int getIdAuthor() {
        return idAuthor;
    }

    /**
     * @param idAuthor 
     */
    public void setIdAuthor(int idAuthor) {
        this.idAuthor = idAuthor;
    }

    /**
     * @return List<String>
     */
    public List<String> getKeywordsAuthor() {
        return keywordsAuthor;
    }

    /**
     * @param keywordsAuthor 
     */
    public void setKeywordsAuthor(List<String> keywordsAuthor) {
        this.keywordsAuthor = keywordsAuthor;
    }
    
   
}
