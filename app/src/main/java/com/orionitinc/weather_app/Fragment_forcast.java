package com.orionitinc.weather_app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment_forcast extends Fragment {


    private RecyclerView listview;
    private List<WeatherForecastResponse.Mylist> forecastDatalist;
    private WeatherForecastAdapter adapter;
    private Context context;

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private String units = "metric";
    private WeatherForecastService weatherForecastService;

    public static Location lastLocation;

    private FusedLocationProviderClient client;

    public Fragment_forcast() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_forcast, container, false);

        listview = (RecyclerView) view.findViewById(R.id.forecast_listView);

        client = LocationServices.getFusedLocationProviderClient(getContext ());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherForecastService = retrofit.create(WeatherForecastService.class);

        getLastLocation();
        return view;
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

                callForecastWeather ();
            }
        });
    }


    private void callForecastWeather() {
        Call<WeatherForecastResponse> responseCall = weatherForecastService.getCurrentForecastWeatherData(
                String.valueOf(lastLocation.getLatitude ()),
                String.valueOf(lastLocation.getLongitude ()),
                units,
                getString(R.string.weather_API_Key)
        );

        responseCall.enqueue(new Callback<WeatherForecastResponse> () {
            @Override
            public void onResponse(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                if(response.code()==200){

                    WeatherForecastResponse forecastData = response.body();
                    forecastDatalist = forecastData.getList();
                    context = getContext();

                    adapter = new WeatherForecastAdapter(context,forecastDatalist);
                    LinearLayoutManager llm = new LinearLayoutManager(getContext());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    listview.setLayoutManager(llm);
                    listview.setAdapter(adapter);
                    Log.e("forecast", "setForecastDataShow: "+forecastDatalist.size() );
                }
            }

            @Override
            public void onFailure(Call<WeatherForecastResponse> call, Throwable t) {
            }
        });

    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible){
            callForecastWeather();
        }
    }

}

