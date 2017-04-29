package com.example.android.booklist;

/**
 * Created by rmhuneineh on 29/04/2017.
 */

public class Book {

    private String mTitle;

    private String mAuthor;

    private double mRating;

    public Book(String title, String author, double rating){
        mTitle = title;
        mAuthor = author;
        mRating = rating;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getAuthor(){
        return mAuthor;
    }

    public double getRating(){
        return mRating;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public void setAuthor(String author){
        mAuthor = author;
    }

    public void setRating(double rating){
        mRating = rating;
    }
}
