package com.cdoak.twitchfiretv.twitchapi;

import java.util.List;

/**
 * Created by cdoak on 8/11/15.
 * Json that holds the top games
 */
public class TopGames {
    public int _total;

    public TopGamesLinks _links;

    public List<TopGame> top;
}
