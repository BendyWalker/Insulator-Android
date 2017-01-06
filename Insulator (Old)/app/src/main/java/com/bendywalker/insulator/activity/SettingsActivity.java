package com.bendywalker.insulator.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bendywalker.insulator.BloodGlucoseUnit;
import com.bendywalker.insulator.Calculator;
import com.bendywalker.insulator.MyPreferenceManager;
import com.bendywalker.insulator.R;
import com.bendywalker.insulator.billing.IabHelper;
import com.bendywalker.insulator.billing.IabResult;
import com.bendywalker.insulator.billing.Inventory;
import com.bendywalker.insulator.billing.Purchase;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingsActivity";
    private static final String SMALL_TIP = "small_tip";
    private static final String LARGE_TIP = "large_tip";

    String[] units;

    RelativeLayout bloodGlucoseUnitContainer, floatingPointCarbohydratesContainer, emailContainer, twitterContainer, googlePlayContainer, smallTipContainer, largeTipContainer;
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
        emailContainer = (RelativeLayout) findViewById(R.id.settings_feedback_email_container);
        twitterContainer = (RelativeLayout) findViewById(R.id.settings_feedback_twitter_container);
        googlePlayContainer = (RelativeLayout) findViewById(R.id.settings_feedback_play_container);
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
        floatingPointCarbohydratesSwitch.setChecked(preferenceManager.allowFloatingPointCarbohydrates());

        bloodGlucoseUnitContainer.setOnClickListener(this);
        floatingPointCarbohydratesContainer.setOnClickListener(this);
        emailContainer.setOnClickListener(this);
        twitterContainer.setOnClickListener(this);
        googlePlayContainer.setOnClickListener(this);
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
                    return;
                }

                if (result.isSuccess()) {
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

                                if (inventory.getPurchase(SMALL_TIP) != null)
                                    iabHelper.consumeAsync(inventory.getPurchase(SMALL_TIP), onConsumeFinishedListener);
                                if (inventory.getPurchase(LARGE_TIP) != null)
                                    iabHelper.consumeAsync(inventory.getPurchase(LARGE_TIP), onConsumeFinishedListener);
                            }
                        }
                    });
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_preferences_blood_glucose_unit_container:
                AlertDialog.Builder bloodGlucoseUnitDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                bloodGlucoseUnitDialog.setItems(units, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        BloodGlucoseUnit bloodGlucoseUnit = preferenceManager.getBloodGlucoseUnit();
                        double desiredBloodGlucose = preferenceManager.getDesiredBloodGlucose();
                        double correctiveFactor = preferenceManager.getCorrectiveFactor();
                        String bloodGlucoseUnitString = units[which];

                        switch (which) {
                            case 0:
                                if (bloodGlucoseUnit != BloodGlucoseUnit.mmol) {
                                    bloodGlucoseUnit = BloodGlucoseUnit.mmol;
                                    desiredBloodGlucose = Calculator.round(desiredBloodGlucose / Calculator.MGDL_CONVERSION_VALUE);
                                    correctiveFactor = Calculator.round(correctiveFactor / Calculator.MGDL_CONVERSION_VALUE);
                                    Log.e(TAG, String.valueOf(correctiveFactor) + " " + String.valueOf(desiredBloodGlucose));
                                }
                                break;
                            case 1:
                                if (bloodGlucoseUnit != BloodGlucoseUnit.mgdl) {
                                    bloodGlucoseUnit = BloodGlucoseUnit.mgdl;
                                    desiredBloodGlucose = Calculator.round(desiredBloodGlucose * Calculator.MGDL_CONVERSION_VALUE);
                                    correctiveFactor = Calculator.round(correctiveFactor * Calculator.MGDL_CONVERSION_VALUE);
                                    Log.e(TAG, String.valueOf(correctiveFactor) + " " + String.valueOf(desiredBloodGlucose));
                                }
                                break;
                        }

                        preferenceManager.setBloodGlucoseUnit(bloodGlucoseUnit);
                        preferenceManager.setDesiredBloodGlucose(desiredBloodGlucose);
                        preferenceManager.setCorrectiveFactor(correctiveFactor);
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
            case R.id.settings_feedback_email_container:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, getResources().getString(R.string.settings_feedback_email_contact));
                startActivity(emailIntent);
                break;
            case R.id.settings_feedback_twitter_container:
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/insulatorapp"));
                startActivity(twitterIntent);
                break;
            case R.id.settings_feedback_play_container:
                final Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
                if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
                    startActivity(rateAppIntent);
                }
                break;
            case R.id.settings_leave_a_tip_small_container:
                smallTipPriceTextView.setVisibility(View.GONE);
                smallTipProgressBar.setVisibility(View.VISIBLE);
                smallTipContainer.setClickable(false);
                if (iabHelper != null) iabHelper.flagEndAsync();
                iabHelper.launchPurchaseFlow(this, SMALL_TIP, 1, onIabPurchaseFinishedListener);
                break;
            case R.id.settings_leave_a_tip_large_container:
                largeTipPriceTextView.setVisibility(View.GONE);
                largeTipProgressBar.setVisibility(View.VISIBLE);
                largeTipContainer.setClickable(false);
                if (iabHelper != null) iabHelper.flagEndAsync();
                iabHelper.launchPurchaseFlow(this, LARGE_TIP, 2, onIabPurchaseFinishedListener);
                break;
        }
    }

    IabHelper.OnIabPurchaseFinishedListener onIabPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.e(TAG, "Purchase finished. Purchase: " + purchase + ", result: " + result);

            if (result.isFailure()) {
                Log.e(TAG, "Failed to complete purchase: " + result);
                smallTipPriceTextView.setVisibility(View.VISIBLE);
                smallTipProgressBar.setVisibility(View.GONE);
                smallTipContainer.setClickable(true);
                largeTipPriceTextView.setVisibility(View.VISIBLE);
                largeTipProgressBar.setVisibility(View.GONE);
                largeTipContainer.setClickable(true);
                return;
            }

            if (result.isSuccess()) {
                iabHelper.consumeAsync(purchase, onConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener onConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            Toast toast = Toast.makeText(getApplicationContext(), R.string.settings_tip_jar_thanks, Toast.LENGTH_LONG);

            Log.e(TAG, result.getMessage());
            if (result.isFailure()) {
                Log.e(TAG, "Failed to consume purchase: " + purchase.toString() + " because " + result);
            }

            if (result.isSuccess()) {
                toast.show();

                if (purchase.getSku().equals(SMALL_TIP)) {
                    smallTipPriceTextView.setVisibility(View.VISIBLE);
                    smallTipProgressBar.setVisibility(View.GONE);
                    smallTipContainer.setClickable(true);
                } else if (purchase.getSku().equals(LARGE_TIP)) {
                    largeTipPriceTextView.setVisibility(View.VISIBLE);
                    largeTipProgressBar.setVisibility(View.GONE);
                    largeTipContainer.setClickable(true);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
                + data);

        // Pass on the activity result to the helper for handling
        if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }
}