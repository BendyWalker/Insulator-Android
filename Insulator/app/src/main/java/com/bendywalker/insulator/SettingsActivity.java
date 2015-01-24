package com.bendywalker.insulator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;


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
