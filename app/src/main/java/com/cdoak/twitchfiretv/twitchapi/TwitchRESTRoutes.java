package com.cdoak.twitchfiretv.twitchapi;

import com.cdoak.twitchfiretv.utils.HTTPHeader;
import com.cdoak.twitchfiretv.utils.URLQueryString;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author cdoak
 * Routes for the Twitch REST API.
 */
public class TwitchRESTRoutes {
    /* Routes */

    public static final String API = "https://api.twitch.tv";

    public static final String TOP_GAMES = API + "/kraken/games/top";

    public static final String STREAMS = API + "/kraken/streams";

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
        String url = String.format(USHER + "api/channel/hls/%s.m3u8%s", channel, query.getQueryString());
        return url;
    }

    /* HTTP Headers */

    public static final HTTPHeader ACCEPT_HEADER = new HTTPHeader("Accept", "application/vnd.twitchtv.v3+json");
}
