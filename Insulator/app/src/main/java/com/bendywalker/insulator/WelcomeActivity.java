package com.bendywalker.insulator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class WelcomeActivity extends ActionBarActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        getFragmentManager().beginTransaction().replace(R.id.container, new WelcomePage1Fragment())
                            .commit();
    }

    public void goForwardToFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_up,
                                                                    R.animator.slide_out_up)
                            .replace(R.id.container, fragment).commit();
    }
}
