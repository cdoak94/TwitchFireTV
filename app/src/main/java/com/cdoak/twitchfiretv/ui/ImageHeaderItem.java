package com.cdoak.twitchfiretv.ui;

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.HeaderItem;

/**
 * Created by Chris Doak on 8/11/2015.
 */
public class ImageHeaderItem extends HeaderItem {
    private Drawable icon;

    public ImageHeaderItem(long id, String name) {
        super(id, name);
    }

    public ImageHeaderItem(String name) {
        super(name);
    }

    public ImageHeaderItem(String name, Drawable ico) {
        super (name);
        icon = ico;
    }

    public Drawable getIcon() {
        return icon;
    }
}
