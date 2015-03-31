package com.example.jephillips.ereader;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by Josh on 3/31/2015.
 */
public class NoteActivity extends Activity implements
        NoteFragment.Contract {
    public static final String EXTRA_POSITION="position";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            int position=getIntent().getIntExtra(EXTRA_POSITION, -1);

            if (position >= 0) {
                Fragment noteFragment=NoteFragment.newInstance(position);
                getFragmentManager().beginTransaction()
                        .add(android.R.id.content, noteFragment).commit();
            }
        }
    }

    public void closeNotes() {
        finish();
    }
}
