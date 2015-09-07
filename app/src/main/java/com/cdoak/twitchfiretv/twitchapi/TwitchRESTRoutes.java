package com.cdoak.twitchfiretv.twitchapi;

import android.app.Activity;
import android.content.Context;

import com.cdoak.twitchfiretv.R;
import com.cdoak.twitchfiretv.utils.HTTPHeader;
import com.cdoak.twitchfiretv.utils.URLQueryString;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author cdoak
 * Routes for the Twitch REST API.
 */
public class TwitchRESTRoutes {

    /* Data */
    public static final String API = "https://api.twitch.tv";

    public static final String API_ROOT = API + "/kraken";

    public static final String TOP_GAMES = API_ROOT + "/games/top";

    public static final String STREAMS = API_ROOT + "/streams";

    public static final String FEATURED_STREAMS = STREAMS + "/featured";

    public static final String FOLLOWED_STREAMS = STREAMS + "/followed";

    public static String requestTopStreams(int limit, int offset) {
        URLQueryString query = new URLQueryString();
        query.add("limit", Integer.toString(limit));
        query.add("offset", Integer.toString(offset));
        return STREAMS + query.getQueryString();
    }

    public static String requestTopGames(int limit, int offset) {
        URLQueryString query = new URLQueryString();
        query.add("limit", Integer.toString(limit));
        query.add("offset", Integer.toString(offset));
        return TOP_GAMES + query.getQueryString();
    }

    public static String requestFeaturedStreams(int limit, int offset) {
        URLQueryString query = new URLQueryString();
        query.add("limit", Integer.toString(limit));
        query.add("offset", Integer.toString(offset));
        return FEATURED_STREAMS + query.getQueryString();
    }

    public static String requestFollowedStreams(int limit, int offset) {
        URLQueryString query = new URLQueryString();
        query.add("limit", Integer.toString(limit));
        query.add("offset", Integer.toString(offset));
        return FOLLOWED_STREAMS + query.getQueryString();
    }


    /* Stream requests */
    public static final String STREAM_TOKEN = API + "/api/channels/%s/access_token";

    public static final String USHER = "http://usher.twitch.tv";

    public static String requestStreamToken(String channel) {
        return String.format(STREAM_TOKEN, channel);
    }

    public static String requestStreamPlaylist(String channel, StreamAccessToken token) {
        URLQueryString query = new URLQueryString();
        query.add("token", token.token);
        query.add("sig", token.sig);
        query.add("type", "any");
        return String.format(USHER + "api/channel/hls/%s.m3u8%s", channel, query.getQueryString());
    }

    /* HTTP Headers */
    public static final HTTPHeader ACCEPT_HEADER = new HTTPHeader("Accept", "application/vnd.twitchtv.v3+json");

    public static HTTPHeader AuthHeader(String token) {
        return new HTTPHeader("Authorization", "OAuth " + token);
    }

    /* Authentication */
    public static final String LOGGED_IN_ID = "LoggedIn";
    public static final String OAUTH_TOKEN_ID = "OauthToken";

    public static final String OAUTH_URL = API_ROOT + "/oauth2/authorize";

    public static final String TOKEN_URL = API_ROOT + "/oauth2/token";

    public static String getOauthUrl(Activity activity) {
        URLQueryString query = new URLQueryString();
        query.add("response_type", "code");
        query.add("client_id", activity.getString(R.string.oauth_client_id));
        query.add("redirect_uri", activity.getString(R.string.oauth_redirect_uri));
        query.add("scope", "user_read user_follows_edit user_subscriptions chat_login");
        return OAUTH_URL + query.getQueryString();
    }

    public static String getTokenUrl(Activity activity, String code) {
        URLQueryString query = new URLQueryString();
        query.add("client_id", activity.getString(R.string.oauth_client_id));
        query.add("client_secret", activity.getString(R.string.oauth_client_secret));
        query.add("grant_type", "authorization_code");
        query.add("redirect_uri", activity.getString(R.string.oauth_redirect_uri));
        query.add("code", code);
        return TOKEN_URL + query.getQueryString();
    }

}
