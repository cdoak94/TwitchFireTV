package com.cdoak.twitchfiretv.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.SearchFragment;

import com.cdoak.twitchfiretv.R;

/**
 * Created by Chris Doak on 8/11/2015.
 * @author cdoak
 * The main search activity for the app.
 */
public class MainSearchActivity extends Activity {
    private MainSearchFragment search_fragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        search_fragment = (MainSearchFragment) getFragmentManager().findFragmentById(R.id.main_search_fragment);
    }

    @Override
    public boolean onSearchRequested() {
        startActivity(new Intent(this, MainSearchActivity.class));
        return true;
    }
}
