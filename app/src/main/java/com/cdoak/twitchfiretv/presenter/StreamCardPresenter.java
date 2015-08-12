package com.cdoak.twitchfiretv.presenter;

import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cdoak.twitchfiretv.R;
import com.cdoak.twitchfiretv.twitchapi.Stream;
import com.cdoak.twitchfiretv.twitchapi.TopGame;
import com.cdoak.twitchfiretv.twitchapi.TopGames;

/**
 * Created by cdoak on 8/12/15.
 */
public class StreamCardPresenter extends Presenter {
    private static int STREAM_PREVIEW_WIDTH = 640;
    private static int STREAM_PREVIEW_HEIGHT = 360;
    private static int selectedBackgroundColor;
    private static int defaultBackgroundColor;
    private Drawable defaultCardImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        defaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        selectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);
        defaultCardImage = parent.getResources().getDrawable(R.drawable.boxart_placeholder);

        ImageCardView cardView = new ImageCardView(parent.getContext()) {
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

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? selectedBackgroundColor : defaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setMainImageDimensions(STREAM_PREVIEW_WIDTH,STREAM_PREVIEW_HEIGHT);
        if (item instanceof Stream) {
            Stream stream = (Stream) item;
            cardView.setTitleText(stream.channel.status);
            cardView.setContentText(stream.channel.name + " playing " + stream.game);
            if (stream.preview != null) {
                Glide.with(viewHolder.view.getContext())
                        .load(stream.preview.large)
                        .centerCrop()
                        .error(defaultCardImage)
                        .into(cardView.getMainImageView());
            }
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}