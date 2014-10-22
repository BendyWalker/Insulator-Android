package com.bendywalker.insulator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity
{
    private static final int NUM_PAGES = 2;
    ViewPager pager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        boolean isFirstTimeOpened = preferences
                .getBoolean(getString(R.string.preference_first_time_open), true);

        if (isFirstTimeOpened)
        {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }
        else
        {
            setContentView(R.layout.activity_main);

            pager = (ViewPager) findViewById(R.id.pager);
            pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
        }
    }


    @Override
    protected void onResume()
    {
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;

            default:
                break;
        }

        return false;
    }

    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter
    {
        public ScreenSlidePagerAdapter(FragmentManager fragmentManager)
        {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return new VariableDataFragment();

                case 1:
                    return new PersistentDataFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount()
        {
            return NUM_PAGES;
        }
    }
}
