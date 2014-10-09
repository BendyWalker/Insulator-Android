package com.bendywalker.insulator;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;


public class SettingsActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.container, new SettingsFragment(), "FRAGMENT_TAG").commit();
    }

    @Override
    protected void onPause()
    {


        super.onPause();
    }
}
