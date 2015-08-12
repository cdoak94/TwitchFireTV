package com.cdoak.twitchfiretv.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.SpeechRecognitionCallback;

import java.lang.reflect.Field;


/**
 * Created by Chris Doak on 8/11/2015.
 */
public class MainSearchFragment extends SearchFragment
        implements SearchFragment.SearchResultProvider {
    private static final int SEARCH_DELAY_MS = 300;
    private ArrayObjectAdapter searchResultsRowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSpeechRecognitionCallback(new SpeechRecognitionCallback() {
            @Override
            public void recognizeSpeech() {
                //do nothing
            }
        });

        searchResultsRowAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return searchResultsRowAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }
}
