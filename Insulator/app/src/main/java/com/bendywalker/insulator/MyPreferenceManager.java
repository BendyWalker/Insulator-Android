package com.bendywalker.insulator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

public class MyPreferenceManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private static String KEY_VERSION_CODE;
    private static String KEY_IS_FIRST_RUN;
    private static String KEY_ALLOW_FLOATING_POINT_CARBOHYDRATES;
    private static String KEY_BLOOD_GLUCOSE_UNIT;
    private static String KEY_CARBOHYDRATE_FACTOR;
    private static String KEY_CORRECTIVE_FACTOR;
    private static String KEY_DESIRED_BLOOD_GLUCOSE;

    public MyPreferenceManager(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        Resources resources = context.getResources();

        KEY_VERSION_CODE = resources.getString(R.string.key_version_code);
        KEY_IS_FIRST_RUN = resources.getString(R.string.key_is_first_run);
        KEY_ALLOW_FLOATING_POINT_CARBOHYDRATES = resources.getString(R.string.key_allow_floating_point_carbohydrates);
        KEY_BLOOD_GLUCOSE_UNIT = resources.getString(R.string.key_blood_glucose_unit);
        KEY_CARBOHYDRATE_FACTOR = resources.getString(R.string.key_carbohydrate_factor);
        KEY_CORRECTIVE_FACTOR = resources.getString(R.string.key_corrective_factor);
        KEY_DESIRED_BLOOD_GLUCOSE = resources.getString(R.string.key_desired_blood_glucose);
    }

    public long getVersionCode() {
        return preferences.getLong(KEY_VERSION_CODE, 0);
    }

    public void setVersionCode(long versionCode) {
        editor.putLong(KEY_VERSION_CODE, versionCode).apply();
    }

    public boolean isFirstRun() {
        return preferences.getBoolean(KEY_IS_FIRST_RUN, true);
    }

    public void setIsFirstRun(boolean isFirstRun) {
        editor.putBoolean(KEY_IS_FIRST_RUN, isFirstRun).apply();
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
        editor.putString(KEY_BLOOD_GLUCOSE_UNIT, bloodGlucoseUnit.toString()).apply();
    }

    public double getCarbohydrateFactor() {
        return Double.valueOf(preferences.getString(KEY_CARBOHYDRATE_FACTOR, "0.0"));
    }

    public void setCarbohydrateFactor(double carbohydrateFactor) {
        editor.putString(KEY_CARBOHYDRATE_FACTOR,  String.valueOf(carbohydrateFactor)).apply();
    }

    public double getCorrectiveFactor() {
        return Double.valueOf(preferences.getString(KEY_CORRECTIVE_FACTOR, "0.0"));
    }

    public void setCorrectiveFactor(double correctiveFactor) {
        editor.putString(KEY_CORRECTIVE_FACTOR, String.valueOf(correctiveFactor)).apply();
    }

    public double getDesiredBloodGlucose() {
        return Double.valueOf(preferences.getString(KEY_DESIRED_BLOOD_GLUCOSE, "0.0"));
    }

    public void setDesiredBloodGlucose(double desiredBloodGlucose) {
        editor.putString(KEY_DESIRED_BLOOD_GLUCOSE, String.valueOf(desiredBloodGlucose)).apply();
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
