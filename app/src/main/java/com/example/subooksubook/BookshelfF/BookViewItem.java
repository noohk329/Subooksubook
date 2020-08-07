package com.example.subooksubook.BookshelfF;

import android.graphics.Bitmap;

public class BookViewItem {
    private String title;
    private String publisher;
    private String author;
    private String bookImage;
    private String date;
    private String progressPercent;
    private int totalpage;

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

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }
    public String getBookImage()
    {
        return bookImage;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getProgressPercent() {return progressPercent;}
    public void setProgressPercent(String progressPercent) {this.progressPercent = progressPercent;}
}
