package com.cdoak.twitchfiretv.presenter;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cdoak.twitchfiretv.R;
import com.cdoak.twitchfiretv.twitchapi.Game;
import com.cdoak.twitchfiretv.twitchapi.TopGame;
import com.cdoak.twitchfiretv.twitchapi.TopGames;

/**
 * Created by cdoak on 8/11/15.
 * @author cdoak
 * A presenter to show the information for an underlying game, shows box art, game title, and viewers.
 */
public class GameCardPresenter extends Presenter {
    private static int GAME_BOX_WIDTH = 272;
    private static int GAME_BOX_HEIGHT = 380;
    private static int selectedBackgroundColor;
    private static int defaultBackgroundColor;
    private Drawable defaultCardImage;
    private Drawable allGamesImage;
    private Drawable gamesIconImage;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        defaultBackgroundColor = parent.getResources().getColor(R.color.default_background);
        selectedBackgroundColor = parent.getResources().getColor(R.color.selected_background);
        defaultCardImage = parent.getResources().getDrawable(R.drawable.boxart_placeholder);
        allGamesImage = parent.getResources().getDrawable(R.drawable.allgames_placeholder);

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
        if (item instanceof TopGame) {
            TopGame topGame = (TopGame) item;
            cardView.setMainImageDimensions(GAME_BOX_WIDTH, GAME_BOX_HEIGHT);
            cardView.setTitleText(topGame.game.name);
            cardView.setContentText(String.format("%,d Viewers", topGame.viewers));
            if (topGame.game.box.large != null) {
                Glide.with(viewHolder.view.getContext())
                        .load(topGame.game.box.large)
                        .centerCrop()
                        .error(defaultCardImage)
                        .into(cardView.getMainImageView());
            }
        } else if (item instanceof TopGames) {
            TopGames topGames = (TopGames) item;
            cardView.setTitleText("Browse All Games");
            cardView.setContentText(topGames._total + " Games Live");
            cardView.setMainImage(allGamesImage);
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }
}
