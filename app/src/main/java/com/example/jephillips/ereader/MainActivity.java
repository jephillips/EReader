package com.example.jephillips.ereader;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;


import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {

    private ViewPager pager=null;
    private ContentsAdapter adapter=null;
    private static final String MODEL = "model";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStrictMode();
        setContentView(R.layout.activity_main);
        pager=(ViewPager)findViewById(R.id.pager);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                pager.setCurrentItem(0, false);
                return (true);

            case R.id.about:
                Intent i = new Intent(this, SimpleContentActivity.class);
                i.putExtra(SimpleContentActivity.EXTRA_FILE,
                        "file:///android_asset/misc/about.html");
                startActivity(i);
                return (true);

            case R.id.help:
                i = new Intent(this, SimpleContentActivity.class);
                i.putExtra(SimpleContentActivity.EXTRA_FILE,
                        "file:///android_asset/misc/help.html");
                startActivity(i);
                return (true);
            case R.id.settings:
                startActivity(new Intent(this, Preferences.class));
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        if (adapter == null) {
            ModelFragment mfrag =
                    (ModelFragment)getFragmentManager().findFragmentByTag(MODEL);
            if (mfrag == null) {
                getFragmentManager().beginTransaction().add(new ModelFragment(), MODEL).commit();
            }
            else if (mfrag.getBook() != null) {
                setupPager(mfrag.getBook());
            }
        }
    }

    @Override
    public void onPause() {
        super.onResume();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(BookLoadedEvent event) {
        setupPager(event.getBook());
    }

    private void setupPager(BookContents contents) {
        adapter = new ContentsAdapter(this, contents);
        pager.setAdapter(adapter);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        pager.setVisibility(View.VISIBLE);
    }

    private void setupStrictMode() {
        StrictMode.ThreadPolicy.Builder builder =
                new StrictMode.ThreadPolicy.Builder().detectNetwork();

        if (BuildConfig.DEBUG) {
            builder.penaltyDeath();
        }
        else {
            builder.penaltyLog();
        }

        StrictMode.setThreadPolicy(builder.build());
    }


}