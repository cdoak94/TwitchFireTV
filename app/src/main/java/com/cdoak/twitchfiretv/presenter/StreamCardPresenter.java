package com.cdoak.twitchfiretv.presenter;

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cdoak.twitchfiretv.R;
import com.cdoak.twitchfiretv.twitchapi.Stream;
import com.cdoak.twitchfiretv.twitchapi.Streams;
import com.cdoak.twitchfiretv.twitchapi.TopGame;
import com.cdoak.twitchfiretv.twitchapi.TopGames;
import com.cdoak.twitchfiretv.view.StreamCardView;

/**
 * Created by cdoak on 8/12/15.
 * @author cdoak
 * Presents a card with a stream on it.
 */
public class StreamCardPresenter extends Presenter {
    private static final int STREAM_PREVIEW_WIDTH = 512;
    private static final int STREAM_PREVIEW_HEIGHT = 304;
    private static int selectedBackgroundColor;
    private static int defaultBackgroundColor;
    private Drawable defaultCardImage;
    private Drawable allGameCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        defaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        selectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);
        defaultCardImage = parent.getResources().getDrawable(R.drawable.channel_placeholder);
        allGameCardImage = parent.getResources().getDrawable(R.drawable.allchannels_placeholder);


        StreamCardView cardView = new StreamCardView(parent.getContext()) {
            @Override
            public void setSelected(boolean selected) {
                updateCardBackgroundColor(this, selected);
                super.setSelected(selected);
            }
        };

        cardView.setFocusable(true);
        cardView.setFocusableInTouchMode(true);
        updateCardBackgroundColor(cardView, false);
        return new ViewHolder(cardView);
    }

    private static void updateCardBackgroundColor(StreamCardView view, boolean selected) {
        int color = selected ? selectedBackgroundColor : defaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        StreamCardView cardView = (StreamCardView) viewHolder.view;
        cardView.setMainImageDimensions(STREAM_PREVIEW_WIDTH,STREAM_PREVIEW_HEIGHT);
        if (item instanceof Stream) {
            Stream stream = (Stream) item;
            cardView.setTitleText(stream.channel.status);
            cardView.setChannelText(stream.channel.name);
            cardView.setGameText(stream.channel.game);
            cardView.setViewerText(String.format("%,d", stream.viewers));
            if (stream.preview != null) {
                Glide.with(viewHolder.view.getContext())
                        .load(stream.preview.medium)
                        .centerCrop()
                        .error(defaultCardImage)
                        .into(cardView.getMainImageView());
            }
        } else if (item instanceof Streams) {
            Streams streams = (Streams) item;
            cardView.setTitleText("Browse All Channels");
            cardView.setMainImage(allGameCardImage);
            cardView.setChannelText(streams._total + " Channels Live");
            cardView.setGameText("-");
            cardView.setViewerText("-");
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        StreamCardView cardView = (StreamCardView) viewHolder.view;
        cardView.setMainImage(null);
    }
}