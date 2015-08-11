package com.cdoak.twitchfiretv.ui;

import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdoak.twitchfiretv.R;
import com.cdoak.twitchfiretv.data.GsonRequest;
import com.cdoak.twitchfiretv.data.VolleySingleton;
import com.cdoak.twitchfiretv.presenter.GameCardPresenter;
import com.cdoak.twitchfiretv.presenter.IconHeaderItemPresenter;
import com.cdoak.twitchfiretv.twitchapi.TopGame;
import com.cdoak.twitchfiretv.twitchapi.TopGames;
import com.cdoak.twitchfiretv.twitchapi.TwitchRESTRoutes;
import com.google.gson.Gson;

/**
 * Created by cdoak on 8/7/15.
 */
public class MainFragment extends BrowseFragment {
    private ArrayObjectAdapter sectionElementsAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadAllData();

        setupUIElements();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void setupUIElements() {
        setBadgeDrawable(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_HIDDEN);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                return new IconHeaderItemPresenter();
            }
        });
    }

    private void loadAllData() {
        sectionElementsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        Log.d("LOADING DATA","GAME");
        loadGamesData();
        setAdapter(sectionElementsAdapter);
    }

    private void loadGamesData() {
        GsonRequest<TopGames> topGamesRequest = new GsonRequest<TopGames>
                (TwitchRESTRoutes.TOP_GAMES,TopGames.class, null, topGamesListener(), errorListener());
        RequestQueue rq = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();
        rq.add(topGamesRequest);

    }

    private Response.Listener<TopGames> topGamesListener() {
        return new Response.Listener<TopGames>() {
            @Override
            public void onResponse(TopGames response) {
                Gson gson = new Gson();
                Log.d("GAMES RESPONSE", gson.toJson(response));
                GameCardPresenter cardPresenter = new GameCardPresenter();

                ArrayObjectAdapter gameListRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (TopGame topGame : response.top) {
                    gameListRowAdapter.add(topGame);
                }

                HeaderItem header = new HeaderItem("Games");
                sectionElementsAdapter.add(new ListRow(header, gameListRowAdapter));
            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GSON", "ERROR");
            }
        };
    }
}
