package fr.alainmuller.volleysample.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import fr.alainmuller.volleysample.app.util.BitmapLruCache;

/**
 * On utilise une classe représentant l'application pour mettre en place la requestQueue et
 * l'ImageLoader de la librairie Volley
 * Created by Alain MULLER on 15/05/2014.
 */
public class VolleySampleApplication extends Application {

    // Queue des opérations de requêtage réseau à jouer par le pool de threads
    private RequestQueue mVolleyRequestQueue;
    // Image Loader gérant le cache pour récupérer des images en ligne
    private ImageLoader mVolleyImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        mVolleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mVolleyImageLoader = new ImageLoader(mVolleyRequestQueue, new BitmapLruCache());
        mVolleyRequestQueue.start();
    }

    public RequestQueue getVolleyRequestQueue() {
        return mVolleyRequestQueue;
    }

    public ImageLoader getVolleyImageLoader() {
        return mVolleyImageLoader;
    }

    @Override
    public void onTerminate() {
        // On va arrêter les opérations à la destruction de l'appli
        mVolleyRequestQueue.stop();
        super.onTerminate();
    }
}
