/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HashSearch.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class IndexBuilder has been created during my internhip in Japan, where
 * I have to create an Hash-search algorithm. This class reprents the Collection
 * object
 *
 * @author angele
 */
public class InCollection {

    private int idInCollect = 0;
    private List<Author> listAuthors = new ArrayList();
    private String title;
    private int idTitle = 0;
    private String pages;
    private int idPages = 0;
    private Integer year;
    private int idYear = 0;
    private String booktitle;
    private int idBooktitle = 0;
    private String url;
    private int idUrl = 0;

    public InCollection() {
    }
    
    /**
     * 
     * @param idInCollect 
     */
    public InCollection(int idInCollect) {
        idInCollect = idInCollect;
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        String inCollection = "";
        inCollection += ("inCollection, " + "id : " + this.getIdInCollect() + "\n");
        for (int i = 0; i < this.getListAuthors().size(); i++) {
            inCollection += ("nom author : " + getListAuthors().get(i).getNom() + ", id author : " + getListAuthors().get(i).getIdAuthor() + "\n");
        }
        inCollection += ("nom title : " + this.getTitle() + " id title : " + this.getIdTitle() + "\n");
        inCollection += ("nom pages : " + this.getPages() + " id pages : " + this.getIdPages() + "\n");
        inCollection += ("nom year : " + this.getYear() + " id year : " + this.getIdYear() + "\n");
        inCollection += ("nom booktitle : " + this.getBooktitle() + " id booktitle : " + this.getIdBooktitle() + "\n");
        inCollection += ("nom url : " + this.getUrl() + " id url : " + this.getIdUrl() + "\n");
        return inCollection;
    }


    
    /**
     * @param authors
     */
    public void setAuthors(List<Author> authors) {
        this.listAuthors = authors;
    }

    /**
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return String
     */
    public String getPages() {
        return pages;
    }

    /**
     * @param pages
     */
    public void setPages(String pages) {
        this.pages = pages;
    }

    /**
     * @return Integer
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return String
     */
    public String getBooktitle() {
        return booktitle;
    }

    /**
     * @param booktitle
     */
    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    /**
     * @return String
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return int
     */
    public int getIdInCollect() {
        return idInCollect;
    }

    /**
     * @param idInCollect
     */
    public void setIdInCollect(int idInCollect) {
        this.idInCollect = idInCollect;
    }

    /**
     * List<Author>
     *
     * @return
     */
    public List<Author> getListAuthors() {
        return listAuthors;
    }

    /**
     * @param listAuthors
     */
    public void setListAuthors(List<Author> listAuthors) {
        this.listAuthors = listAuthors;
    }

  

    /**
     * @return int
     */
    public int getIdTitle() {
        return idTitle;
    }

    /**
     * @param idTitle
     */
    public void setIdTitle(int idTitle) {
        this.idTitle = idTitle;
    }

    /**
     * @return int
     */
    public int getIdPages() {
        return idPages;
    }

    /**
     * @param idPages
     */
    public void setIdPages(int idPages) {
        this.idPages = idPages;
    }

    /**
     * @return int
     */
    public int getIdYear() {
        return idYear;
    }

    /**
     * @param idYear
     */
    public void setIdYear(int idYear) {
        this.idYear = idYear;
    }

    /**
     * @return int
     */
    public int getIdBooktitle() {
        return idBooktitle;
    }

    /**
     * @param idBooktitle
     */
    public void setIdBooktitle(int idBooktitle) {
        this.idBooktitle = idBooktitle;
    }

    /**
     * @return int
     */
    public int getIdUrl() {
        return idUrl;
    }

    /**
     * @param idUrl
     */
    public void setIdUrl(int idUrl) {
        this.idUrl = idUrl;
    }

}
