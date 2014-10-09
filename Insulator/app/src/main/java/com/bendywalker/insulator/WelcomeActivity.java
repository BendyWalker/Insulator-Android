package com.bendywalker.insulator;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;


public class WelcomeActivity extends Activity
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
        getFragmentManager().beginTransaction().setCustomAnimations(R.animator.slide_in_left,
                                                                    R.animator.slide_out_right)
                            .replace(R.id.container, fragment).commit();
    }
}
