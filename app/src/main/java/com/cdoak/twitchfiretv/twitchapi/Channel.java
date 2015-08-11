package com.cdoak.twitchfiretv.twitchapi;

/**
 * Created by cdoak on 8/7/15.
 */
public class Channel {
    public int _id;
    public int delay;

    public int views;
    public int followers;

    public boolean partner;
    public boolean mature;

    public String broadcaster_language;
    public String created_at;
    public String updated_at;

    public String name;
    public String status;
    public String display_name;
    public String game;
    public String url;

    public String logo;
    public String banner;
    public String video_banner;
    public String background;
    public String profile_banner;
    public String profile_banner_background_color;

    public ChannelLinks _links;

    public Channel() {

    }
}
