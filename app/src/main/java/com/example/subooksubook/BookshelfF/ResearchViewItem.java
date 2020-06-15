package com.example.subooksubook.BookshelfF;

import android.graphics.Bitmap;

public class ResearchViewItem {

    private String title;
    private String publisher;
    private String author;
    private String description;
    private Bitmap bookImage;


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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
}
