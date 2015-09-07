package com.cdoak.twitchfiretv.twitchapi;

import java.util.List;

/**
 * Created by Chris Doak on 9/5/2015.
 * @author Chris Doak
 * Json object that holds the oauth token response.
 */
public class Oauth2TokenResponse {
    public String access_token;
    public String refresh_token;
    public List<String> scope;
}
