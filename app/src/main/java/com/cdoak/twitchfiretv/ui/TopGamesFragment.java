package com.cdoak.twitchfiretv.ui;

import android.os.Bundle;

import com.cdoak.twitchfiretv.R;

/**
 * Created by cdoak on 8/12/15.
 * @author cdoak
 */
public class TopGamesFragment extends android.support.v17.leanback.app.VerticalGridFragment {
    private static final int NUM_COLUMNS = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.top_games_fragment_title));
    }
}
