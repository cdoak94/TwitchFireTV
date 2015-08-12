package com.cdoak.twitchfiretv.twitchapi;

import com.cdoak.twitchfiretv.utils.HTTPHeader;

import java.util.Map;

/**
 * @author cdoak
 * Routes for the Twitch REST API.
 */
public class TwitchRESTRoutes {
    /* Routes */

    public static final String API = "https://api.twitch.tv/kraken";

    public static final String TOP_GAMES = API + "/games/top";

    public static final String STREAMS = API + "/streams";


    /* HTTP Headers */

    public static final HTTPHeader ACCEPT_HEADER = new HTTPHeader("Accept", "application/vnd.twitchtv.v3+json");
}
