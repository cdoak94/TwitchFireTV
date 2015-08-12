package com.cdoak.twitchfiretv.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * @author cdoak
 * A singleton class to hold the android volley request queue as per the recommendation for volley
 * usage.
 */
public class VolleySingleton {

    private static VolleySingleton instance = null;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context context;

    private VolleySingleton(Context c) {
        context = c;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * A method to get the volley singleton instance.
     * @param c The application context.
     * @return The volley singleton instance.
     */
    public static synchronized VolleySingleton getInstance(Context c) {
        if(instance == null) {
            instance = new VolleySingleton(c);
        }
        return instance;
    }

    /**
     * Gets the volley request queue.
     * @return The volley singleton request queue.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Helper to add to the request queue easily
     * @param req Android volley request.
     * @param <T> The type of the volley request.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * The singleton imageLoader, because I use glide, it probably doesn't get used.
     * @return Singleton's image loader.
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}
