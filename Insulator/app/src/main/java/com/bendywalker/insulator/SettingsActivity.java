package com.bendywalker.insulator;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class SettingsActivity extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
    }

    @Override
    protected void onPause()
    {


        super.onPause();
    }
}
