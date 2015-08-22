package com.cdoak.twitchfiretv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v17.leanback.widget.SparseArrayObjectAdapter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cdoak.twitchfiretv.R;
import com.cdoak.twitchfiretv.data.GsonRequest;
import com.cdoak.twitchfiretv.data.VolleySingleton;
import com.cdoak.twitchfiretv.presenter.GameCardPresenter;
import com.cdoak.twitchfiretv.presenter.IconHeaderItemPresenter;
import com.cdoak.twitchfiretv.presenter.StreamCardPresenter;
import com.cdoak.twitchfiretv.twitchapi.FeaturedStream;
import com.cdoak.twitchfiretv.twitchapi.FeaturedStreams;
import com.cdoak.twitchfiretv.twitchapi.Stream;
import com.cdoak.twitchfiretv.twitchapi.Streams;
import com.cdoak.twitchfiretv.twitchapi.TopGame;
import com.cdoak.twitchfiretv.twitchapi.TopGames;
import com.cdoak.twitchfiretv.twitchapi.TwitchRESTRoutes;
import com.cdoak.twitchfiretv.utils.HTTPHeader;
import com.cdoak.twitchfiretv.utils.HTTPHeaders;

import java.util.HashMap;

/**
 * @author cdoak
 * This is where the main landing page does all of it's rendering magic.
 */
public class MainFragment extends BrowseFragment {
    private SparseArrayObjectAdapter sectionElementsAdapter;
    private PresenterSelector headerPresenterSelector;
    private RequestQueue volleyRQ;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        volleyRQ = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

        loadAllData();
        setupEventListeners();
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
        setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.ic_twitch_logo_white));
        setTitle(getString(R.string.app_name));
        setHeadersState(HEADERS_HIDDEN);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        headerPresenterSelector = new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object item) {
                return new IconHeaderItemPresenter();
            }
        };

        setHeaderPresenterSelector(headerPresenterSelector);
    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainSearchActivity.class);
                startActivity(intent);
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());

    }

    private void loadAllData() {
        sectionElementsAdapter = new SparseArrayObjectAdapter(new ListRowPresenter());
        Log.d("LOADING DATA", "FEATURED");
        loadFeaturedData();
        Log.d("LOADING DATA", "GAME");
        loadGamesData();
        Log.d("LOADING DATA", "STREAMS");
        loadStreamsData();
        setAdapter(sectionElementsAdapter);
    }

    private void loadFeaturedData() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        GsonRequest<FeaturedStreams> featuredStreamsRequest = new GsonRequest<>
                (TwitchRESTRoutes.FEATURED_STREAMS, FeaturedStreams.class, headers, featuredStreamsListener(), errorListener());
        volleyRQ.add(featuredStreamsRequest);

    }

    private Response.Listener<FeaturedStreams> featuredStreamsListener() {
        return new Response.Listener<FeaturedStreams>() {
            @Override
            public void onResponse(FeaturedStreams featuredStreams) {
                StreamCardPresenter cardPresenter = new StreamCardPresenter();

                ArrayObjectAdapter streamsListRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (FeaturedStream stream : featuredStreams.featured) {
                    streamsListRowAdapter.add(stream.stream);
                }

                HeaderItem header = new ImageHeaderItem(getResources().getString(R.string.featured_row_title), getResources().getDrawable(R.drawable.ic_featured));
                sectionElementsAdapter.set(0, new ListRow(header, streamsListRowAdapter));
            }
        };
    }

    private void loadGamesData() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        GsonRequest<TopGames> topGamesRequest = new GsonRequest<TopGames>
                (TwitchRESTRoutes.TOP_GAMES, TopGames.class, headers, topGamesListener(), errorListener());
        volleyRQ.add(topGamesRequest);
    }

    private Response.Listener<TopGames> topGamesListener() {
        return new Response.Listener<TopGames>() {
            @Override
            public void onResponse(TopGames response) {
                GameCardPresenter cardPresenter = new GameCardPresenter();

                ArrayObjectAdapter gameListRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (TopGame topGame : response.top) {
                    gameListRowAdapter.add(topGame);
                }
                gameListRowAdapter.add(response);

                HeaderItem header = new ImageHeaderItem(getResources().getString(R.string.games_row_header), getResources().getDrawable(R.drawable.ic_games));
                sectionElementsAdapter.set(1, new ListRow(header, gameListRowAdapter));
            }
        };
    }

    private void loadStreamsData(){
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        GsonRequest<Streams> topStreamsRequest = new GsonRequest<Streams>
                (TwitchRESTRoutes.requestTopStreams(10), Streams.class, headers, streamsListener(), errorListener());
        volleyRQ.add(topStreamsRequest);
    }

    private Response.Listener<Streams> streamsListener() {
        return new Response.Listener<Streams>() {
            @Override
            public void onResponse(Streams streams) {
                StreamCardPresenter cardPresenter = new StreamCardPresenter();

                ArrayObjectAdapter streamsListRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (Stream stream : streams.streams) {
                    streamsListRowAdapter.add(stream);
                }
                streamsListRowAdapter.add(streams);

                HeaderItem header = new ImageHeaderItem(getResources().getString(R.string.streams_row_title), getResources().getDrawable(R.drawable.ic_channels));
                sectionElementsAdapter.set(2, new ListRow(header, streamsListRowAdapter));
            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GSON", "ERROR" + error.toString());
            }
        };
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof TopGame) {
                Toast.makeText(getActivity(), ((TopGame)item).game.name, Toast.LENGTH_SHORT).show();
            } else if (item instanceof TopGames) {
                Toast.makeText(getActivity(), ((TopGames)item).toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
