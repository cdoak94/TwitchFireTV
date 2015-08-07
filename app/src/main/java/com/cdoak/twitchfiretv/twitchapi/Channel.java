package com.cdoak.twitchfiretv.twitchapi;

/**
 * Created by cdoak on 8/7/15.
 */
public class Channel {
    int _id;
    int delay;

    int views;
    int followers;

    boolean partner;
    boolean mature;

    String broadcaster_language;
    String created_at;
    String updated_at;

    String name;
    String status;
    String display_name;
    String game;
    String url;

    String logo;
    String banner;
    String video_banner;
    String background;
    String profile_banner;
    String profile_banner_background_color;

    ChannelLinks _links;

    public Channel() {

    }
}
