package com.example.android.booklist;

/**
 * Created by rmhuneineh on 29/04/2017.
 */

public class Book {

    private String mTitle;

    private String mAuthor;

    public Book(String title, String author){
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public void setAuthor(String author){
        mAuthor = author;
    }
}
