package com.example.shasta.sunshine;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.shasta.sunshine.data.WeatherContract;

public class ForecastFragment extends Fragment {
    private ForecastAdapter mForecastAdapter ;
    public ForecastFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {   super.onCreate(savedInstanceState);
        setHasOptionsMenu(true) ;    }

   @Override
   public void onCreateOptionsMenu(Menu menu,MenuInflater inflater )
   {       inflater.inflate(R.menu.forecastfragment, menu) ;  }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
        updateWeather();
            return true;        }

return super.onOptionsItemSelected(item) ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);


        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.ListViewForecast);
        listView.setAdapter(mForecastAdapter) ;
        return rootView;
    }

    private void updateWeather (){
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }

    @Override
    public void onStart(){
        super.onStart() ;
        updateWeather() ;
    }
}