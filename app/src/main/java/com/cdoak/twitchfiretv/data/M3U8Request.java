package com.cdoak.twitchfiretv.data;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;


import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by cdoak on 8/13/15.
 * @author cdoak
 * A volley request for an M3U8 playlist.
 */
public class M3U8Request extends Request<M38UPlaylist> {
    private final Map<String, String> headers;
    private final Listener<M38UPlaylist> listener;

    public M3U8Request(String url, Map<String, String> headers, Listener<M38UPlaylist> listener,
                       ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<M38UPlaylist> parseNetworkResponse(NetworkResponse response) {
        try {
            String playlist = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(M38UPlaylist response) {
        listener.onResponse(response);
    }
}
