package com.cdoak.twitchfiretv.twitchapi;

/**
 * Created by cdoak on 8/12/15.
 * @author cdoak
 * Represents a twitch api stream object
 */
public class Stream {
    public String game;

    public long _id;
    public int viewers;

    public double average_fps;
    public int video_height;

    public String created_at;

    public StreamPreview preview;

    public StreamLinks _links;

    public Channel channel;
}
