package com.bendywalker.insulator;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;


public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
    Preference bloodGlucoseUnitPreference;
    MyPreferenceManager preferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        LinearLayout bar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent()
                    .getParent()
                    .getParent();
            bar = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.preferences_toolbar, root, false);
            root.addView(bar, 0);
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.preferences_toolbar, root, false);
            root.addView(bar);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue
                        .complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            } else {
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
        }

        preferenceManager = new MyPreferenceManager(getApplicationContext());

        bloodGlucoseUnitPreference = findPreference(
                getString(R.string.key_blood_glucose_unit));
        bloodGlucoseUnitPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String value = (String) newValue;
        BloodGlucoseUnit bloodGlucoseUnit = BloodGlucoseUnit.valueOf(value);
        double savedDesiredBloodGlucose = preferenceManager.getDesiredBloodGlucose();
        double savedCorrectiveFactor = preferenceManager.getCorrectiveFactor();

        switch (bloodGlucoseUnit) {
            case mmol:
                savedDesiredBloodGlucose = savedDesiredBloodGlucose / 18;
                savedCorrectiveFactor = savedCorrectiveFactor / 18;
                break;
            case mgdl:
                savedDesiredBloodGlucose = savedDesiredBloodGlucose * 18;
                savedCorrectiveFactor = savedCorrectiveFactor * 18;
        }

        preferenceManager.setDesiredBloodGlucose(savedDesiredBloodGlucose);
        preferenceManager.setCorrectiveFactor(savedCorrectiveFactor);

        return true;
    }
}
