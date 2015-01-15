package com.bendywalker.insulator;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener
{
    Preference bloodGlucoseMeasurementPreference;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

bloodGlucoseMeasurementPreference = findPreference(getActivity().getString(R.string.preference_blood_glucose_units));
        bloodGlucoseMeasurementPreference.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue)
    {
        
        return false;
    }
}
