package com.cdoak.twitchfiretv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.BaseCardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.cdoak.twitchfiretv.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by cdoak on 8/13/15.
 * @author cdoak
 * A remake of image card view to show more details about a stream.
 */
public class StreamCardView extends BaseCardView {
    private ImageView mImageView;
    private View mInfoArea;
    private TextView mTitleView;
    private TextView channelTextView;
    private TextView gameTextView;
    private TextView viewerTextView;
    private ImageView channelBadge;
    private ImageView gameBadge;
    private ImageView viewerBadge;

    private List<ImageView> badges = new ArrayList<ImageView>();

    public StreamCardView(Context context) {
        this(context, null);
    }

    public StreamCardView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v17.leanback.R.attr.imageCardViewStyle);
    }

    public StreamCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.stream_card_view, this);

        mImageView = (ImageView) v.findViewById(R.id.main_image);
        mImageView.setVisibility(View.INVISIBLE);
        mInfoArea = v.findViewById(R.id.info_field);
        mTitleView = (TextView) v.findViewById(R.id.title_text);
        gameTextView = (TextView) v.findViewById(R.id.game_text);
        channelTextView = (TextView) v.findViewById(R.id.channel_text);
        viewerTextView = (TextView) v.findViewById(R.id.views_text);

        channelBadge = (ImageView) v.findViewById(R.id.channel_badge);
        gameBadge = (ImageView) v.findViewById(R.id.game_badge);
        viewerBadge =  (ImageView) v.findViewById(R.id.viewer_badge);
        badges.add(channelBadge);
        badges.add(gameBadge);
        badges.add(viewerBadge);

        if (mInfoArea != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.lbImageCardView,
                    defStyle, 0);
            try {
                setInfoAreaBackground(
                        a.getDrawable(R.styleable.lbImageCardView_infoAreaBackground));
            } finally {
                a.recycle();
            }
        }
    }

    public final ImageView getMainImageView() {
        return mImageView;
    }

    public void setMainImageAdjustViewBounds(boolean adjustViewBounds) {
        if (mImageView != null) {
            mImageView.setAdjustViewBounds(adjustViewBounds);
        }
    }

    public void setMainImageScaleType(ScaleType scaleType) {
        if (mImageView != null) {
            mImageView.setScaleType(scaleType);
        }
    }

    /**
     * Set drawable with fade-in animation.
     */
    public void setMainImage(Drawable drawable) {
        setMainImage(drawable, true);
    }

    /**
     * Set drawable with optional fade-in animation.
     */
    public void setMainImage(Drawable drawable, boolean fade) {
        if (mImageView == null) {
            return;
        }

        mImageView.setImageDrawable(drawable);
        if (drawable == null) {
            mImageView.animate().cancel();
            mImageView.setAlpha(1f);
            mImageView.setVisibility(View.INVISIBLE);
        } else {
            mImageView.setVisibility(View.VISIBLE);
            if (fade) {
                fadeIn(mImageView);
            } else {
                mImageView.animate().cancel();
                mImageView.setAlpha(1f);
            }
        }
    }

    public void setMainImageDimensions(int width, int height) {
        ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mImageView.setLayoutParams(lp);
    }

    public Drawable getMainImage() {
        if (mImageView == null) {
            return null;
        }

        return mImageView.getDrawable();
    }

    public Drawable getInfoAreaBackground() {
        if (mInfoArea != null) {
            return mInfoArea.getBackground();
        }
        return null;
    }

    public void setInfoAreaBackground(Drawable drawable) {
        if (mInfoArea != null) {
            mInfoArea.setBackground(drawable);
            for (ImageView badge : badges) {
                if (badge != null){
                    badge.setBackground(drawable);
                }
            }
        }
    }

    public void setInfoAreaBackgroundColor(int color) {
        if (mInfoArea != null) {
            mInfoArea.setBackgroundColor(color);
            for (ImageView badge : badges) {
                if (badge != null) {
                    badge.setBackgroundColor(color);
                }
            }
        }
    }

    public void setTitleText(CharSequence text) {
        if (mTitleView == null) return;

        mTitleView.setText(text);
        setTextMaxLines();
    }

    public CharSequence getTitleText() {
        if (mTitleView == null) return null;

        return mTitleView.getText();
    }

    public void setChannelText(CharSequence text) {
        if (channelTextView == null) return;

        channelTextView.setText(text);
    }

    public CharSequence getChannelText() {
        if (channelTextView == null) return null;

        return channelTextView.getText();
    }

    public void setGameText(CharSequence text) {
        if (gameTextView == null) return;

        gameTextView.setText(text);
    }

    public CharSequence getGameText() {
        if (gameTextView == null) return null;

        return gameTextView.getText();
    }

    public void setViewerText(CharSequence text) {
        if (viewerTextView == null) return;

        viewerTextView.setText(text);
    }

    private void fadeIn(View v) {
        v.setAlpha(0f);
        v.animate().alpha(1f).setDuration(v.getContext().getResources().getInteger(
                android.R.integer.config_shortAnimTime)).start();
    }

    @Override
    public boolean hasOverlappingRendering() {
        return false;
    }

    private void setTextMaxLines() {

    }

    @Override
    protected void onDetachedFromWindow() {
        mImageView.animate().cancel();
        mImageView.setAlpha(1f);
        super.onDetachedFromWindow();
    }
}
