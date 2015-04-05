package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class MyPreferenceManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static String KEY_IS_FIRST_TIME_OPEN;
    private static String KEY_ALLOW_FLOATING_POINT_CARBOHYDRATES;
    private static String KEY_BLOOD_GLUCOSE_UNIT;
    private static String KEY_CARBOHYDRATE_FACTOR;
    private static String KEY_CORRECTIVE_FACTOR;
    private static String KEY_DESIRED_BLOOD_GLUCOSE;

    public MyPreferenceManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        Resources resources = context.getResources();
        KEY_IS_FIRST_TIME_OPEN = resources.getString(R.string.key_is_first_time_open);
        KEY_ALLOW_FLOATING_POINT_CARBOHYDRATES = resources.getString(R.string.key_allow_floating_point_carbohydrates);
        KEY_BLOOD_GLUCOSE_UNIT = resources.getString(R.string.key_blood_glucose_unit);
        KEY_CARBOHYDRATE_FACTOR = resources.getString(R.string.key_carbohydrate_factor);
        KEY_CORRECTIVE_FACTOR = resources.getString(R.string.key_corrective_factor);
        KEY_DESIRED_BLOOD_GLUCOSE = resources.getString(R.string.key_desired_blood_glucose);
    }

    public boolean isFirstTimeOpen() {
        return preferences.getBoolean(KEY_IS_FIRST_TIME_OPEN, true);
    }

    public void setIsFirstTimeOpen(boolean isFirstTimeOpen) {
        editor.putBoolean(KEY_IS_FIRST_TIME_OPEN, isFirstTimeOpen).apply();
    }

    public boolean allowFloatingPointCarbohydrates() {
        return preferences.getBoolean(KEY_ALLOW_FLOATING_POINT_CARBOHYDRATES, false);
    }

    public void setAllowFloatingPointCarbohydrates(boolean allowFloatingPointCarbohydrates) {
        editor.putBoolean(KEY_ALLOW_FLOATING_POINT_CARBOHYDRATES, allowFloatingPointCarbohydrates).apply();
    }

    public BloodGlucoseUnit getBloodGlucoseUnit() {
        return BloodGlucoseUnit.valueOf(preferences.getString(KEY_BLOOD_GLUCOSE_UNIT, "mmol"));
    }

    public void setBloodGlucoseUnit(BloodGlucoseUnit bloodGlucoseUnit) {
        editor.putString(KEY_BLOOD_GLUCOSE_UNIT, bloodGlucoseUnit.toString());
    }

    public double getCarbohydrateFactor() {
        return preferences.getFloat(KEY_CARBOHYDRATE_FACTOR, 0);
    }

    public void setCarbohydrateFactor(double carbohydrateFactor) {
        editor.putFloat(KEY_CARBOHYDRATE_FACTOR, (float) carbohydrateFactor);
    }

    public double getCorrectiveFactor() {
        return preferences.getFloat(KEY_CORRECTIVE_FACTOR, 0);
    }

    public void setCorrectiveFactor(double correctiveFactor) {
        editor.putFloat(KEY_CORRECTIVE_FACTOR, (float) correctiveFactor);
    }

    public double getDesiredBloodGlucose() {
        return preferences.getFloat(KEY_DESIRED_BLOOD_GLUCOSE, 0);
    }

    public void setDesiredBloodGlucose(double desiredBloodGlucose) {
        editor.putFloat(KEY_DESIRED_BLOOD_GLUCOSE, (float) desiredBloodGlucose);
    }

    public void setValueFromKey(double value, String key) {
        if (key.equals(KEY_CARBOHYDRATE_FACTOR)) {
            setCarbohydrateFactor(value);
        } else if (key.equals(KEY_CORRECTIVE_FACTOR)) {
            setCorrectiveFactor(value);
        } else if (key.equals(KEY_DESIRED_BLOOD_GLUCOSE)) {
            setDesiredBloodGlucose(value);
        }
    }
}
