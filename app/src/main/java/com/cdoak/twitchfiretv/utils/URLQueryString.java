package com.cdoak.twitchfiretv.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by cdoak on 8/13/15.
 * @author cdoak
 * A class to help generate a query string. Also encodes in UTF-8
 */
public class URLQueryString {
    public static final String ENCODING = "UTF-8";
    private ArrayList<MapEntry> queryPairs;

    public void add(String key, String value) {
        queryPairs.add(new MapEntry(key, value));
    }

    public String getQueryString() {
        StringBuilder query = new StringBuilder();
        query.append("?");
        for (int i = 0; i < queryPairs.size(); i++) {
            query.append(urlEncode(queryPairs.get(i).getKey()));
            query.append("=");
            query.append(urlEncode(queryPairs.get(i).getValue()));
            if (i != (queryPairs.size() - 1)) query.append("&");
        }
        return query.toString();
    }

    private String urlEncode(String toEncode) {
        String encoded;
        try {
            encoded = URLEncoder.encode(toEncode, ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return toEncode;
        }
        return encoded;
    }

    private class MapEntry implements Map.Entry<String, String> {
        private String key;
        private String value;

        public MapEntry(String k, String v) {
            key = k;
            value = v;
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
        public String setValue(String object) {
            return null;
        }
    }

}
