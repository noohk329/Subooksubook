package com.example.subooksubook;

import android.graphics.Bitmap;

public class BookViewItem {
    private String title;
    private String publisher;
    private String author;
    private Bitmap bookImage;
    private String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBookImage(Bitmap bookImage) {
        this.bookImage = bookImage;
    }
    public Bitmap getBookImage()
    {
        return bookImage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
