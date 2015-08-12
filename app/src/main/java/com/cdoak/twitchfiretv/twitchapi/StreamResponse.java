package com.cdoak.twitchfiretv.twitchapi;

/**
 * Created by cdoak on 8/12/15.
 * @author cdoak
 * Json returned when a single stream is requested, contains a Stream and StreamLinks
 */
public class StreamResponse {
    public StreamResponseLinks _links;

    public Stream stream;

    public StreamResponse() {

    }
}
