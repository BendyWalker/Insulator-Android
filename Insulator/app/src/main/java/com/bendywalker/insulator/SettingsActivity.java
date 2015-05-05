package com.bendywalker.insulator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bendywalker.insulator.billing.IabHelper;
import com.bendywalker.insulator.billing.IabResult;
import com.bendywalker.insulator.billing.Inventory;
import com.bendywalker.insulator.billing.Purchase;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends ActionBarActivity implements View.OnClickListener {
    private static final String TAG = "SettingsActivity";
    private static final String SMALL_TIP = "small_tip";
    private static final String LARGE_TIP = "large_tip";

    String[] units;

    RelativeLayout bloodGlucoseUnitContainer, floatingPointCarbohydratesContainer, smallTipContainer, largeTipContainer;
    TextView bloodGlucoseUnitTextView, smallTipPriceTextView, largeTipPriceTextView;
    ProgressBar smallTipProgressBar, largeTipProgressBar;
    SwitchCompat floatingPointCarbohydratesSwitch;

    IabHelper iabHelper;
    MyPreferenceManager preferenceManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        units = getResources().getStringArray(R.array.preference_blood_glucose_units_entries);
        preferenceManager = new MyPreferenceManager(this);

        bloodGlucoseUnitContainer = (RelativeLayout) findViewById(R.id.settings_preferences_blood_glucose_unit_container);
        floatingPointCarbohydratesContainer = (RelativeLayout) findViewById(R.id.settings_preferences_floating_point_carbohydrates_container);
        smallTipContainer = (RelativeLayout) findViewById(R.id.settings_leave_a_tip_small_container);
        largeTipContainer = (RelativeLayout) findViewById(R.id.settings_leave_a_tip_large_container);

        bloodGlucoseUnitTextView = (TextView) findViewById(R.id.settings_preferences_blood_glucose_unit_selected);
        smallTipPriceTextView = (TextView) findViewById(R.id.settings_leave_a_tip_small_price);
        largeTipPriceTextView = (TextView) findViewById(R.id.settings_leave_a_tip_large_price);

        smallTipProgressBar = (ProgressBar) findViewById(R.id.settings_leave_a_tip_small_progress);
        largeTipProgressBar = (ProgressBar) findViewById(R.id.settings_leave_a_tip_large_progress);

        floatingPointCarbohydratesSwitch = (SwitchCompat) findViewById(R.id.settings_preferences_floating_point_carbohydrates_switch);

        String bloodGlucoseUnitString;
        switch (preferenceManager.getBloodGlucoseUnit()) {
            case mmol:
                bloodGlucoseUnitString = units[0];
                break;
            case mgdl:
                bloodGlucoseUnitString = units[1];
                break;
            default:
                bloodGlucoseUnitString = units[0];
        }
        bloodGlucoseUnitTextView.setText(bloodGlucoseUnitString);

        bloodGlucoseUnitContainer.setOnClickListener(this);
        floatingPointCarbohydratesContainer.setOnClickListener(this);
        smallTipContainer.setOnClickListener(this);
        largeTipContainer.setOnClickListener(this);
        floatingPointCarbohydratesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferenceManager.setAllowFloatingPointCarbohydrates(isChecked);
            }
        });

        String[] licenseKeyArray = getResources().getStringArray(R.array.license_key);
        StringBuilder licenseKey = new StringBuilder();
        for (String substring : licenseKeyArray) {
            licenseKey.append(substring);
        }

        iabHelper = new IabHelper(this, licenseKey.toString());
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    Log.e(TAG, "Failed to set up in-app billing: " + result);
                }

                List<String> skuList = new ArrayList<>();
                skuList.add(SMALL_TIP);
                skuList.add(LARGE_TIP);
                iabHelper.queryInventoryAsync(true, skuList, new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        if (result.isFailure()) {
                            Log.e(TAG, "Failed to query inventory: " + result);
                        } else {
                            String smallTipPrice = inventory.getSkuDetails(SMALL_TIP).getPrice();
                            String largeTipPrice = inventory.getSkuDetails(LARGE_TIP).getPrice();
                            smallTipPriceTextView.setText(smallTipPrice);
                            largeTipPriceTextView.setText(largeTipPrice);
                            smallTipProgressBar.setVisibility(View.GONE);
                            largeTipProgressBar.setVisibility(View.GONE);
                            smallTipPriceTextView.setVisibility(View.VISIBLE);
                            largeTipPriceTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) iabHelper.dispose();
        iabHelper = null;
    }


    @Override
    public void onClick(View v) {
        final Toast toast = Toast.makeText(this, R.string.settings_leave_a_tip_thanks, Toast.LENGTH_LONG);

        switch (v.getId()) {
            case R.id.settings_preferences_blood_glucose_unit_container:
                AlertDialog.Builder bloodGlucoseUnitDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                bloodGlucoseUnitDialog.setItems(units, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BloodGlucoseUnit bloodGlucoseUnit;
                        String bloodGlucoseUnitString = units[which];

                        switch (which) {
                            case 0:
                                bloodGlucoseUnit = BloodGlucoseUnit.mmol;
                                break;
                            case 1:
                                bloodGlucoseUnit = BloodGlucoseUnit.mgdl;
                                break;
                            default:
                                bloodGlucoseUnit = BloodGlucoseUnit.mmol;
                                break;
                        }

                        double desiredBloodGlucose = preferenceManager.getDesiredBloodGlucose();
                        double correctiveFactor = preferenceManager.getCorrectiveFactor();

                        switch (bloodGlucoseUnit) {
                            case mmol:
                                desiredBloodGlucose = desiredBloodGlucose / 18;
                                correctiveFactor = correctiveFactor / 18;
                                break;
                            case mgdl:
                                desiredBloodGlucose = desiredBloodGlucose * 18;
                                correctiveFactor = correctiveFactor * 18;
                        }

                        preferenceManager.setDesiredBloodGlucose(desiredBloodGlucose);
                        preferenceManager.setCorrectiveFactor(correctiveFactor);
                        preferenceManager.setBloodGlucoseUnit(bloodGlucoseUnit);
                        bloodGlucoseUnitTextView.setText(bloodGlucoseUnitString);

                        dialog.dismiss();
                    }
                });
                bloodGlucoseUnitDialog.create().show();
                break;
            case R.id.settings_preferences_floating_point_carbohydrates_container:
                floatingPointCarbohydratesSwitch.setChecked(!floatingPointCarbohydratesSwitch.isChecked());
                preferenceManager.setAllowFloatingPointCarbohydrates(floatingPointCarbohydratesSwitch.isChecked());
                break;
            case R.id.settings_leave_a_tip_small_container:
                if (iabHelper != null) iabHelper.flagEndAsync();
                iabHelper.launchPurchaseFlow(this, SMALL_TIP, 1, new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                        if (result.isFailure()) {
                            Log.e(TAG, "Failed to complete purchase: " + result);
                        } else {
                            iabHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                                @Override
                                public void onConsumeFinished(Purchase purchase, IabResult result) {
                                    if (result.isSuccess()) {
                                        Log.d(TAG, "Successfully consumed purchase!");
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            case R.id.settings_leave_a_tip_large_container:
                if (iabHelper != null) iabHelper.flagEndAsync();
                iabHelper.launchPurchaseFlow(this, LARGE_TIP, 1, new IabHelper.OnIabPurchaseFinishedListener() {
                    @Override
                    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                        if (result.isFailure()) {
                            Log.e(TAG, "Failed to complete purchase: " + result);
                        } else {
                            iabHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
                                @Override
                                public void onConsumeFinished(Purchase purchase, IabResult result) {
                                    if (result.isSuccess()) {
                                        Log.d(TAG, "Successfully consumed purchase!");
                                    }
                                }
                            });
                        }
                    }
                });
                break;
            default:
                Log.d(TAG, "Default case called in onClick");
                break;
        }
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