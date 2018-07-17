package com.orionitinc.weather_app;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager weatherViewPager;
    private TabLayout weatherTabLayout;
    private WeatherTabPageAdapter tabPageAdapter;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        weatherViewPager = findViewById(R.id.weatherViewPager);
        weatherTabLayout = findViewById(R.id.weatherTabLayout);

        weatherTabLayout.addTab(weatherTabLayout.newTab().setText("Current "));
        weatherTabLayout.addTab(weatherTabLayout.newTab().setText("Forecast "));


        tabPageAdapter = new WeatherTabPageAdapter(getSupportFragmentManager (),weatherTabLayout.getTabCount());

        weatherViewPager.setAdapter(tabPageAdapter);

        weatherTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                weatherViewPager.setCurrentItem(tab.getPosition(),true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        weatherViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(weatherTabLayout));

    }



    public class WeatherTabPageAdapter extends FragmentPagerAdapter {

        private int totalTab;
        public WeatherTabPageAdapter( FragmentManager fm , int totalTab) {
            super(fm);
            this.totalTab = totalTab;
        }

        @Override
        public Fragment getItem( int position) {
            switch (position){
                case 0:
                    return new Fragment_weather ();
                case 1:
                    return new Fragment_forcast ();
            }

            return null;
        }

        @Override
        public int getCount() {
            return totalTab;
        }

    }

}
