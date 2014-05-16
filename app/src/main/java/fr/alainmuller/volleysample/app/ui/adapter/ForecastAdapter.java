package fr.alainmuller.volleysample.app.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.alainmuller.volleysample.app.R;
import fr.alainmuller.volleysample.app.ui.activity.MainActivity;

/**
 * Classe représentant un Adapter pour chaque prévision météo de la liste récupérée
 * Created by Alain MULLER on 16/05/2014.
 */
public class ForecastAdapter extends BaseAdapter {

    private final Context mContext;
    private final ImageLoader mVolleyImageLoader;
    private JSONArray mForecastList;

    public ForecastAdapter(Context mContext, ImageLoader mVolleyImageLoader) {
        this.mContext = mContext;
        this.mVolleyImageLoader = mVolleyImageLoader;
    }

    public void updateForecast(JSONArray forecasts) {
        mForecastList = forecasts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (mForecastList == null) ? 0 : mForecastList.length();
    }

    @Override
    public JSONObject getItem(int position) {
        JSONObject item = null;
        if (mForecastList != null) {
            try {
                item = mForecastList.getJSONObject(position);
            } catch (JSONException e) {
                Log.e(this.getClass().getSimpleName(), "Exception JSON sur la récupération de l'objet en [" + position + "] : " + e.getMessage());
            }
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView forecast;
        NetworkImageView icon;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.forecast_list_item, parent, false);
            forecast = (TextView) convertView.findViewById(R.id.tv_forecast);
            icon = (NetworkImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(new ViewHolder(forecast, icon));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            forecast = viewHolder.getForecast();
            icon = viewHolder.getIcon();
        }

        // On récupère les données du JSONObject et on les insère dans les view
        JSONObject jsonObject = getItem(position);
        forecast.setText(jsonObject.optString(MainActivity.KEY_FORECAST).trim());
        icon.setImageUrl(jsonObject.optString(MainActivity.KEY_ICON_URL), mVolleyImageLoader);

        return convertView;
    }

    static final class ViewHolder {
        private final TextView mForecast;
        private final NetworkImageView mIcon;

        public ViewHolder(TextView forecast, NetworkImageView icon) {
            mForecast = forecast;
            mIcon = icon;
        }

        public TextView getForecast() {
            return mForecast;
        }

        public NetworkImageView getIcon() {
            return mIcon;
        }
    }
}
