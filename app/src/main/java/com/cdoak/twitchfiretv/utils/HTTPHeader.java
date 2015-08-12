package com.cdoak.twitchfiretv.utils;

import java.util.Map;

/**
 * Created by cdoak on 8/12/15.
 * @author cdoak
 * Helper class to store an HTTP Header
 */
public class HTTPHeader implements Map.Entry<String,String> {

    private String key;
    private String value;

    public HTTPHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String setValue(String val) {
        value = val;
        return val;
    }
}
