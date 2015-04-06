package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;


public class WelcomeActivity extends ActionBarActivity {
    public MyPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        preferenceManager = new MyPreferenceManager(getApplicationContext());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new WelcomePage1Fragment())
                .commit();
    }

    public void goForwardToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, fragment).commit();
    public MyPreferenceManager getPreferenceManager() {
        return preferenceManager;
    }
}
