package com.orionitinc.weather_app;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orionitinc.weather_app.animation.AnimationUtil;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.orionitinc.weather_app.R;


public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.forcastViewHolder> {

    private Context context;
    private List<WeatherForecastResponse.Mylist> forecastDataList;
    int  previousposition=0;


    public WeatherForecastAdapter( Context context, List<WeatherForecastResponse.Mylist> forecastData) {
        this.context = context;
        this.forecastDataList = forecastData;
    }

    @Override
    public forcastViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v=inflater.inflate (R.layout.single_weather_forecast_row,parent,false);
        return new forcastViewHolder(v);
    }

    @Override
    public void onBindViewHolder(forcastViewHolder holder, int position) {

        Date date = new Date (forecastDataList.get(position).getDt() * 1000L);
        DateFormat format = new SimpleDateFormat ("dd-MM-yyy");
        format.setTimeZone(TimeZone.getDefault());
        String formatted = format.format(date);

        Date date1 = new Date (forecastDataList.get(position).getDt() * 1000L);
        DateFormat format1 = new SimpleDateFormat ("hh:mm:ss a");
        format1.setTimeZone(TimeZone.getDefault());
        String formatted1 = format1.format(date1);

        Date date2 = new Date (forecastDataList.get(position).getDt() * 1000L);
        SimpleDateFormat format2 = new SimpleDateFormat ("EEEE");
        String day = format2.format(date2);

        holder.forcastDateTV.setText(formatted);
        holder.forcastTimeTV.setText(formatted1);
        holder.forecast_dayTV.setText(day);

        holder.forecast_tempTV.setText(forecastDataList.get(position).getMain().getTemp().toString()+(char) 0x00B0 +"C");
        holder.forcastdisTV.setText(forecastDataList.get(position).getWeather().get(0).getDescription().toString());


        String iconString = forecastDataList.get(position).getWeather().get(0).getIcon();
        Uri iconUri = Uri.parse("http://openweathermap.org/img/w/"+iconString+".png");
        Picasso.with(context)
                .load(iconUri)
                .into(holder.forcastImg);

        if(position > previousposition){

            AnimationUtil.animate(holder,true);
        }
        else{
            AnimationUtil.animate(holder,false);
        }
        previousposition = position;

    }

    @Override
    public int getItemCount() {
        return forecastDataList.size();
    }

    public class forcastViewHolder extends RecyclerView.ViewHolder{

        TextView forcastDateTV;
        TextView forcastdisTV;
        ImageView forcastImg;
        TextView forcastTimeTV;
        TextView forecast_tempTV;
        TextView forecast_dayTV;

        public forcastViewHolder(View itemView) {
            super(itemView);
            forcastDateTV = itemView.findViewById(R.id.forecast_date);
            forcastdisTV = itemView.findViewById(R.id.forecast_discription);
            forcastImg = itemView.findViewById(R.id.forecast_img);
            forcastTimeTV = itemView.findViewById(R.id.forecast_time);
            forecast_tempTV = itemView.findViewById(R.id.forecast_temp);
            forecast_dayTV = itemView.findViewById(R.id.forecast_day);

        }
    }
}
