package com.cdoak.twitchfiretv.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
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
import com.cdoak.twitchfiretv.twitchapi.Oauth2TokenResponse;
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
    private static final int GAMES_TO_DISPLAY_AMOUNT = 15;
    private static final int STREAMS_TO_DISPLAY_AMOUNT = 15;

    private SparseArrayObjectAdapter sectionElementsAdapter;
    private PresenterSelector headerPresenterSelector;
    private RequestQueue volleyRQ;
    private SharedPreferences prefs;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        volleyRQ = VolleySingleton.getInstance(getActivity().getApplicationContext()).getRequestQueue();

        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);

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
        Log.d("LOADING DATA", "USER DATA");
        loadUserData();
        setAdapter(sectionElementsAdapter);
    }

    private void loadFeaturedData() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        GsonRequest<FeaturedStreams> featuredStreamsRequest = new GsonRequest<>
                (TwitchRESTRoutes.requestFeaturedStreams(STREAMS_TO_DISPLAY_AMOUNT, 0),
                        FeaturedStreams.class, headers, featuredStreamsListener(), errorListener());
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

                HeaderItem header = new ImageHeaderItem(getResources().getString(R.string.featured_row_title),
                        getResources().getDrawable(R.drawable.ic_featured));
                sectionElementsAdapter.set(0, new ListRow(header, streamsListRowAdapter));
            }
        };
    }

    private void loadGamesData() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        GsonRequest<TopGames> topGamesRequest = new GsonRequest<TopGames>
                (TwitchRESTRoutes.requestTopGames(GAMES_TO_DISPLAY_AMOUNT, 0),
                        TopGames.class, headers, topGamesListener(), errorListener());
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

                HeaderItem header = new ImageHeaderItem(getResources().getString(R.string.games_row_header),
                        getResources().getDrawable(R.drawable.ic_games));
                sectionElementsAdapter.set(1, new ListRow(header, gameListRowAdapter));
            }
        };
    }

    private void loadStreamsData() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        GsonRequest<Streams> topStreamsRequest = new GsonRequest<Streams>
                (TwitchRESTRoutes.requestTopStreams(STREAMS_TO_DISPLAY_AMOUNT, 0), Streams.class,
                        headers, streamsListener(), errorListener());
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

                HeaderItem header = new ImageHeaderItem(
                        getResources().getString(R.string.streams_row_title),
                        getResources().getDrawable(R.drawable.ic_channels));
                sectionElementsAdapter.set(2, new ListRow(header, streamsListRowAdapter));
            }
        };
    }

    private void loadUserData() {
        boolean loggedIn = prefs.getBoolean(TwitchRESTRoutes.LOGGED_IN_ID, false);
        if (loggedIn) {
            Toast.makeText(getActivity(), "Logged IN", Toast.LENGTH_SHORT).show();
            loadFollowedStreams();
        } else {
            Toast.makeText(getActivity(), "NOT Logged In", Toast.LENGTH_SHORT).show();
            loginOauth();
        }
    }

    private void loadFollowedStreams() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
        headers.add(
                TwitchRESTRoutes.AuthHeader(
                        prefs.getString(TwitchRESTRoutes.OAUTH_TOKEN_ID, "abc123")));
        GsonRequest<Streams> followingStreamsRequest = new GsonRequest<Streams>
                (TwitchRESTRoutes.requestFollowedStreams(STREAMS_TO_DISPLAY_AMOUNT, 0), Streams.class,
                        headers, followedStreamsListener(), errorListener());
        volleyRQ.add(followingStreamsRequest);
    }

    private Response.Listener<Streams> followedStreamsListener() {
        return new Response.Listener<Streams>() {
            @Override
            public void onResponse(Streams streams) {
                StreamCardPresenter cardPresenter = new StreamCardPresenter();

                ArrayObjectAdapter streamsListRowAdapter = new ArrayObjectAdapter(cardPresenter);
                for (Stream stream : streams.streams) {
                    streamsListRowAdapter.add(stream);
                }
                streamsListRowAdapter.add(streams);

                HeaderItem header = new ImageHeaderItem(String.format(
                        getResources().getString(R.string.following_row_title), streams._total),
                        getResources().getDrawable(R.drawable.ic_following));
                sectionElementsAdapter.set(3, new ListRow(header, streamsListRowAdapter));
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

    private void loginOauth() {
        final Dialog auth_dialog = new Dialog(getActivity());
        auth_dialog.setContentView(R.layout.auth_dialog);
        WebView webView = (WebView)auth_dialog.findViewById(R.id.auth_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(TwitchRESTRoutes.getOauthUrl(getActivity()));


        webView.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;
            String authCode;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("?code=") && authComplete != true) {
                    auth_dialog.dismiss();
                    Uri uri = Uri.parse(url);
                    authCode = uri.getQueryParameter("code");
                    authComplete = true;

                    HTTPHeaders headers = new HTTPHeaders();
                    headers.add(TwitchRESTRoutes.ACCEPT_HEADER);
                    GsonRequest<Oauth2TokenResponse> authTokenRequest = new GsonRequest<Oauth2TokenResponse>
                            (Request.Method.POST, TwitchRESTRoutes.getTokenUrl(getActivity(), authCode),
                                    Oauth2TokenResponse.class, headers, authListener(), errorListener());
                    volleyRQ.add(authTokenRequest);

                    Toast.makeText(getActivity(), "Auth Code: " + authCode, Toast.LENGTH_SHORT).show();
                } else if (url.contains("error=access_denied")) {
                    authComplete = true;
                    Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();

                    auth_dialog.dismiss();
                }
            }

            private Response.Listener<Oauth2TokenResponse> authListener() {
                return new Response.Listener<Oauth2TokenResponse>() {
                    @Override
                    public void onResponse(Oauth2TokenResponse oauth2TokenResponse) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(TwitchRESTRoutes.LOGGED_IN_ID, true);
                        editor.putString(TwitchRESTRoutes.OAUTH_TOKEN_ID, oauth2TokenResponse.access_token);
                        editor.commit();
                    }
                };
            }
        });
        auth_dialog.show();
        auth_dialog.setTitle("Log In");
        auth_dialog.setCancelable(true);
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
