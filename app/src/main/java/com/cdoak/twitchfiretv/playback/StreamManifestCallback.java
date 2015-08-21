package com.cdoak.twitchfiretv.playback;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.hls.HlsMasterPlaylist;
import com.google.android.exoplayer.hls.HlsPlaylist;
import com.google.android.exoplayer.util.ManifestFetcher;

import java.io.IOException;

/**
 * Created by cdoak on 8/14/15.
 * @author cdoak
 * class to handle manifest callbacks from exoplayer or something.
 */
public class StreamManifestCallback implements ManifestFetcher.ManifestCallback<HlsPlaylist> {

    private String url;
    private String other;
    private MediaCodecVideoTrackRenderer.EventListener eventListener;
    private Context context;
    private Handler handler;

    public StreamManifestCallback(MediaCodecVideoTrackRenderer.EventListener eventListener, Context context, Handler handler, String url) {
        this.url = url;
        this.context = context;
        this.eventListener = eventListener;
        this.handler = handler;
    }

    @Override
    public void onSingleManifest(HlsPlaylist hlsPlaylist) {
        if (hlsPlaylist instanceof HlsMasterPlaylist) {
            //do-nothing yet, maybe quality controls later... You got me.
        }
    }

    @Override
    public void onSingleManifestError(IOException e) {

    }
}
