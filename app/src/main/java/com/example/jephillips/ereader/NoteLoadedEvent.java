package com.example.jephillips.ereader;

/**
 * Created by Josh on 3/31/2015.
 */
public class NoteLoadedEvent {
    int position;
    String prose;

    NoteLoadedEvent(int position, String prose) {
        this.position = position;
        this.prose = prose;
    }

    int getPosition() {
        return(position);
    }

    String getProse() {
        return(prose);
    }
}
