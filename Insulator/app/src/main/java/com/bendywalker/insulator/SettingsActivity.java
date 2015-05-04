package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.bendywalker.insulator.billing.IabHelper;
import com.bendywalker.insulator.billing.IabResult;

public class SettingsActivity extends ActionBarActivity {
    private static final String TAG = "SettingsActivity";

    IabHelper iabHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] licenseKeyArray = getResources().getStringArray(R.array.license_key);
        StringBuilder licenseKey = new StringBuilder();
        for (String substring : licenseKeyArray) {
            licenseKey.append(substring);
        }

        iabHelper = new IabHelper(this, licenseKey.toString());
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(TAG, "Problem setting up In-app Billing: " + result);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }
}


//public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
//    Preference bloodGlucoseUnitPreference;
//    MyPreferenceManager preferenceManager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.preferences);
//
//        LinearLayout bar;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent()
//                    .getParent()
//                    .getParent();
//            bar = (LinearLayout) LayoutInflater.from(this)
//                    .inflate(R.layout.preferences_toolbar, root, false);
//            root.addView(bar, 0);
//        } else {
//            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
//            ListView content = (ListView) root.getChildAt(0);
//
//            root.removeAllViews();
//
//            bar = (LinearLayout) LayoutInflater.from(this)
//                    .inflate(R.layout.preferences_toolbar, root, false);
//            root.addView(bar);
//
//            int height;
//            TypedValue tv = new TypedValue();
//            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
//                height = TypedValue
//                        .complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
//            } else {
//                height = bar.getHeight();
//            }
//
//            content.setPadding(0, height, 0, 0);
//
//            root.addView(content);
//        }
//
//        preferenceManager = new MyPreferenceManager(getApplicationContext());
//
//        bloodGlucoseUnitPreference = findPreference(
//                getString(R.string.key_blood_glucose_unit));
//        bloodGlucoseUnitPreference.setOnPreferenceChangeListener(this);
//    }
//
//    @Override
//    public boolean onPreferenceChange(Preference preference, Object newValue) {
//        String value = (String) newValue;
//        BloodGlucoseUnit bloodGlucoseUnit = BloodGlucoseUnit.valueOf(value);
//        double savedDesiredBloodGlucose = preferenceManager.getDesiredBloodGlucose();
//        double savedCorrectiveFactor = preferenceManager.getCorrectiveFactor();
//
//        switch (bloodGlucoseUnit) {
//            case mmol:
//                savedDesiredBloodGlucose = savedDesiredBloodGlucose / 18;
//                savedCorrectiveFactor = savedCorrectiveFactor / 18;
//                break;
//            case mgdl:
//                savedDesiredBloodGlucose = savedDesiredBloodGlucose * 18;
//                savedCorrectiveFactor = savedCorrectiveFactor * 18;
//        }
//
//        preferenceManager.setDesiredBloodGlucose(savedDesiredBloodGlucose);
//        preferenceManager.setCorrectiveFactor(savedCorrectiveFactor);
//
//        return true;
//    }
//}