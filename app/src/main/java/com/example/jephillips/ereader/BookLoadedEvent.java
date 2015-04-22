package com.example.jephillips.ereader;

/**
 * Created by jephillips on 3/24/15.
 */
public class BookLoadedEvent {
    private BookContents contents=null;

    public BookLoadedEvent(BookContents contents) {
        this.contents=contents;
    }

    public BookContents getBook() {
        return contents;
    }
}

//git test
