package com.bendywalker.insulator.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.bendywalker.insulator.MyPreferenceManager;
import com.bendywalker.insulator.R;
import com.bendywalker.insulator.fragment.WelcomePage1Fragment;


public class WelcomeActivity extends AppCompatActivity {
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
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.container, fragment).commit();
    }

    public MyPreferenceManager getPreferenceManager() {
        return preferenceManager;
    }
}
