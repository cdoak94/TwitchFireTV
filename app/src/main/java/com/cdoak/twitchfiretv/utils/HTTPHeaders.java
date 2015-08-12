package com.cdoak.twitchfiretv.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cdoak on 8/12/15.
 * @author cdoak
 * A hasmap of HTTP Headers
 */
public class HTTPHeaders extends HashMap<String, String> {
    public void add(Map.Entry<String, String> httpHeader){
        this.put(httpHeader.getKey(), httpHeader.getValue());
    }
}
