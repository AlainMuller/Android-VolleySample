package fr.alainmuller.volleysample.app.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * Cache d'images personnalisé qu'on va fournir à Volley pour l'ImageLoader
 * cf. http://stackoverflow.com/questions/16682595/android-volley-imageloader-bitmaplrucache-parameter
 * ou encore le tuto sur le blog Xebia : http://blog.xebia.fr/2013/07/08/android-appels-reseau-avec-volley/
 * Created by Alain MULLER on 15/05/2014.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    public BitmapLruCache() {
        this(getDefaultLruCacheSize());
    }

    public BitmapLruCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
