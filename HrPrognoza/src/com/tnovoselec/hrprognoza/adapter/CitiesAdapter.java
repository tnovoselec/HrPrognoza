package com.tnovoselec.hrprognoza.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tnovoselec.hrprognoza.Config;
import com.tnovoselec.hrprognoza.R;
import com.tnovoselec.hrprognoza.data.MockData;
import com.tnovoselec.hrprognoza.model.City;
import com.tnovoselec.hrprognoza.model.DailyForecast;
import com.tnovoselec.hrprognoza.utils.ImageCacheManager;

import java.util.List;

/**
 * Created by tomislav on 19/11/13.
 */
public class CitiesAdapter extends BaseAdapter {

    private List<City> cities;
    private LayoutInflater inflater;

    public CitiesAdapter(Context context, List<City> cities){
        this.cities=cities;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View v = view;
        if (v == null){
            v = inflater.inflate(R.layout.list_item, viewGroup, false);
        }
        City city = cities.get(i);
        DailyForecast item = city.getDailyForecast();
        TextView name = (TextView)v.findViewById(R.id.name);
        TextView temp =(TextView)v.findViewById(R.id.temp);
        NetworkImageView image = (NetworkImageView)v.findViewById(R.id.image);

        name.setText(city.getName());
        temp.setText(item.getForecasts().get(0).getWeather().get(0).getMain());
        image.setImageUrl(String.format(Config.ICON_URL, MockData.getIcon(i)), ImageCacheManager.getInstance().getImageLoader());
        return v;
    }
}
