package com.orionitinc.weather_app;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_weather extends Fragment {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private String units = "metric";
    private WeatherService weatherService;
    private WeatherForecastService weatherForecastService;

    private TextView currentWeatherTV;
    private TextView currentWeatherLocationTV;
    private TextView currentTempTV;
    private TextView currentHumidityTV;
    private TextView currentPressureTV;
    private TextView currentTemp1TV;
    private TextView currentTempMinTV;
    private TextView currentTempMaxTV;
    private TextView currentSunriseTV;
    private TextView currentSunsetTV;
    private TextView currentWindTV;
    private ImageView currentWeatherIcon;

    public static Location lastLocation;
    public static String lat,longi;
    private FusedLocationProviderClient client;

    public Fragment_weather() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_weather, container, false);

        currentWeatherTV = view.findViewById(R.id.currentWeather);
        currentWeatherLocationTV = view.findViewById(R.id.currentWeatherLocation);
        currentTempTV = view.findViewById(R.id.currentTemp);
        currentWeatherIcon = view.findViewById(R.id.currentWeatherIcon);
        currentHumidityTV = view.findViewById(R.id.currentHumidity);
        //currentVisibilityTV = view.findViewById(R.id.currentVisibility);
        currentPressureTV = view.findViewById(R.id.currentPressure);
        currentTemp1TV = view.findViewById(R.id.currentTemp1);
        currentTempMinTV = view.findViewById(R.id.currentTempMin);
        currentTempMaxTV = view.findViewById(R.id.currentTempMax);
        currentSunriseTV = view.findViewById(R.id.currentSunrise);
        currentSunsetTV = view.findViewById(R.id.currentSunset);
        currentWindTV = view.findViewById(R.id.currentWind);


        client = LocationServices.getFusedLocationProviderClient(getContext ());


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        weatherService = retrofit.create(WeatherService.class);
        weatherForecastService = retrofit.create(WeatherForecastService.class);

        final FloatingSearchView floatingSearchView = view.findViewById(R.id.floating_search_view_weather);
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                if(currentQuery != null && currentQuery != ""){
                    Geocoder geocoderSerach = new Geocoder(getContext());
                    List<Address> addresslist = null;
                    try {
                        addresslist = geocoderSerach.getFromLocationName(currentQuery,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(addresslist.size()!=0){
                        Address address = addresslist.get(0) ;
                        lat=String.valueOf(address.getLatitude());
                        longi=String.valueOf(address.getLongitude());
                        callWeatherResponse();
                        floatingSearchView.clearQuery();
                    }

                    else {
                        Toast.makeText(getContext(),"Enter Valid Location", Toast.LENGTH_LONG).show();
                        floatingSearchView.clearQuery();
                    }

                }
            }
        });

        getLastLocation();
        return  view;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getContext (), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext (), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity (),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},21);
            return;
        }
        client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location> () {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                lastLocation = task.getResult();

                callWeatherResponse();
            }
        });
    }



    private void callWeatherResponse() {

            Call<WeatherResponse> call = weatherService.getCurrentWeatherData(
                    String.valueOf(lastLocation.getLatitude ()),
                    String.valueOf(lastLocation.getLongitude ()),
                    units,
                    getString(R.string.weather_API_Key)
            );

            call.enqueue(new Callback<WeatherResponse> () {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if(response.code() == 200){
                        WeatherResponse weatherResponse = response.body();
                        String iconString = weatherResponse.getWeather().get(0).getIcon();

                        Picasso.with(getContext())
                                .load(Uri.parse("http://openweathermap.org/img/w/"+iconString+".png"))
                                .into(currentWeatherIcon);
                        currentWeatherTV.setText(weatherResponse.getWeather().get(0).getMain());
                        currentWeatherLocationTV.setText(weatherResponse.getName());
                        currentTempTV.setText(String.valueOf(weatherResponse.getMain().getTemp()) + (char) 0x00B0 +"C");
                        currentHumidityTV.setText(String.valueOf(weatherResponse.getMain().getHumidity())+" %");
                        //  currentVisibilityTV.setText(String.valueOf(weatherResponse.getVisibility()/1000)+" km");
                        currentPressureTV.setText(String.valueOf(weatherResponse.getMain().getPressure())+" hPa");
                        currentTemp1TV.setText(String.valueOf(weatherResponse.getMain().getTemp()) + (char) 0x00B0 +"C");
                        currentTempMinTV.setText(String.valueOf(weatherResponse.getMain().getTempMin()) + (char) 0x00B0 +"C");
                        currentTempMaxTV.setText(String.valueOf(weatherResponse.getMain().getTempMax()) + (char) 0x00B0 +"C");

                        Date date1 = new Date(weatherResponse.getSys().getSunrise() * 1000L);
                        DateFormat format1 = new SimpleDateFormat ("hh:mm:ss a");
                        format1.setTimeZone(TimeZone.getDefault());
                        String formatted1 = format1.format(date1);

                        Date date2 = new Date(weatherResponse.getSys().getSunset() * 1000L);
                        DateFormat format2 = new SimpleDateFormat("hh:mm:ss a");
                        format2.setTimeZone(TimeZone.getDefault());
                        String formatted2 = format2.format(date2);

                        currentSunriseTV.setText(formatted1);
                        currentSunsetTV.setText(formatted2);
                        currentWindTV.setText(String.valueOf(weatherResponse.getWind().getSpeed()+" m/s"));

                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {

                }
            });

    }

}
