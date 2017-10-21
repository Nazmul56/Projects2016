package com.droidking.nazmul.librarymanagementsystem;

/**
 * Created by Nazmul Haque on 15/04/16.
 */
public class RecyclerAllBorrowDataHolder {
    private String title, genre, year,mem_name, book_title;

    public RecyclerAllBorrowDataHolder() {
    }

    public RecyclerAllBorrowDataHolder(String title, String genre, String year, String mem_name, String book_title) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.mem_name= mem_name;
        this.book_title= book_title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMemName() {
        return mem_name;
    }

    public void setMemName(String mem_name) {
        this.mem_name = mem_name;
    }

    public String getBookTitle() {
        return book_title;
    }

    public void setBookTitle(String book_title) {
        this.book_title = book_title;
    }
}
