package fr.alainmuller.volleysample.app.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import fr.alainmuller.volleysample.app.R;
import fr.alainmuller.volleysample.app.VolleySampleApplication;
import fr.alainmuller.volleysample.app.ui.adapter.ForecastAdapter;

public class MainActivity extends ListActivity {

    public static final String KEY_FORECAST = "fcttext_metric";
    public static final String KEY_ICON_URL = "icon_url";
    private static final String JSON_URL = "http://www.psyckoz.info/tmp/forecast.json";
    private RequestQueue mRequestQueue;
    private ForecastAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On récupère notre RequestQueue et notre ImageLoader depuis notre objet VolleySampleApplication
        VolleySampleApplication app = (VolleySampleApplication) getApplication();
        mRequestQueue = app.getVolleyRequestQueue();
        ImageLoader imageLoader = app.getVolleyImageLoader();

        mAdapter = new ForecastAdapter(app, imageLoader);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // On va créer une nouvelle request à traiter par Volley
        //FIXME ce serait plus propre de refactorer ce code à l'extérieur de l'activity
        JsonArrayRequest request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                // Ce code est appelé quand la requête à été exécutée avec succès,
                // Étant ici dans le thread principal, on va pouvoir mettre à jour notre Adapter
                mAdapter.updateForecast(jsonArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // Le code suivant est appelé lorsque Volley n'a pas réussi à récupérer le résultat de la requête
                Toast.makeText(MainActivity.this, "Erreur de chargement du JSON : " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(this);

        // On ajoute la Request au RequestQueue pour la lancer
        mRequestQueue.add(request);
    }

    @Override
    protected void onStop() {
        mRequestQueue.cancelAll(this);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
