package com.bendywalker.insulator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;


public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener
{
    Preference bloodGlucoseMeasurementPreference;
    SharedPreferences preferences;

    String desiredBloodGlucoseLevelKey, carbohydrateFactorKey, correctiveFactorKey;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        LinearLayout bar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.preferences_toolbar, root, false);
            root.addView(bar, 0);
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.preferences_toolbar, root, false);
            root.addView(bar);

            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }else{
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        desiredBloodGlucoseLevelKey = getString(R.string.preference_desired_blood_glucose_level);
        carbohydrateFactorKey = getString(R.string.preference_carbohydrate_factor);
        correctiveFactorKey = getString(R.string.preference_corrective_factor);

        bloodGlucoseMeasurementPreference = findPreference(
                getString(R.string.preference_blood_glucose_units));
        bloodGlucoseMeasurementPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        String value = (String) newValue;
        boolean isValueMmol = value.equals("mmol");
        SharedPreferences.Editor editor = preferences.edit();

        float savedDesiredBloodGlucoseLevelFloat = preferences
                .getFloat(desiredBloodGlucoseLevelKey, 0);
        float savedCorrectiveFactorFloat = preferences.getFloat(correctiveFactorKey, 0);

        if (isValueMmol)
        {
            savedDesiredBloodGlucoseLevelFloat = savedDesiredBloodGlucoseLevelFloat / 18;
            savedCorrectiveFactorFloat = savedCorrectiveFactorFloat / 18;
        }
        else if (!isValueMmol)
        {
            savedDesiredBloodGlucoseLevelFloat = savedDesiredBloodGlucoseLevelFloat * 18;
            savedCorrectiveFactorFloat = savedCorrectiveFactorFloat * 18;
        }

        editor.putFloat(desiredBloodGlucoseLevelKey,
                        savedDesiredBloodGlucoseLevelFloat);
        editor.putFloat(correctiveFactorKey, savedCorrectiveFactorFloat);

        editor.apply();

        return true;
    }
}
