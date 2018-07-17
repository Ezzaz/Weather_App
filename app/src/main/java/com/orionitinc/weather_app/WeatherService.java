package com.orionitinc.weather_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherService {
    @GET("weather?")
    Call<WeatherResponse> getCurrentWeatherData( @Query("lat") String lat,
                                                 @Query("lon") String lon,
                                                 @Query("units") String units,
                                                 @Query("appid") String appid );
}
