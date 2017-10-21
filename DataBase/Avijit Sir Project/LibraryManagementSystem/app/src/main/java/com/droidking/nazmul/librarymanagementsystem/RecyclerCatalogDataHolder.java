package com.droidking.nazmul.librarymanagementsystem;

/**
 * Created by Lincoln on 15/01/16.
 */
public class RecyclerCatalogDataHolder {
    private String title, author, total,available, book_id , pub_name;

    public RecyclerCatalogDataHolder() {
    }

    public RecyclerCatalogDataHolder(String book_id, String title, String author, String total, String available, String pub_name) {
        this.title = title;
        this.author = author;
        this.total = total;
        this.available= available;
        this.book_id =book_id;
        this.pub_name = pub_name;

    }
    public String getBook_id(){
        return book_id;
    }

    public void setBook_id(String book_id){
        this.book_id= book_id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTotalBook() {
        return total;
    }

    public void setTotalBook(String total) {
        this.total = total;
    }

    public String getAuthors() {
        return author;
    }

    public void setGenre(String author) {
        this.author = author;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getPub_name(){
        return pub_name;
    }

    public void setPub_name(String book_id){
        this.pub_name= pub_name;
    }

}
