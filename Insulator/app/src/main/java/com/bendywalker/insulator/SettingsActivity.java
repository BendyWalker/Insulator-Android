package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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


    IabHelper iabHelper;
    TextView smallTipTitleTextView, smallTipPriceTextView, smallTipDescriptionTextView;
    TextView largeTipTitleTextView, largeTipPriceTextView, largeTipDescriptionTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        smallTipTitleTextView = (TextView) findViewById(R.id.small_tip_title);
        smallTipPriceTextView = (TextView) findViewById(R.id.small_tip_price);
        smallTipDescriptionTextView = (TextView) findViewById(R.id.small_tip_description);
        largeTipTitleTextView = (TextView) findViewById(R.id.large_tip_title);
        largeTipPriceTextView = (TextView) findViewById(R.id.large_tip_price);
        largeTipDescriptionTextView = (TextView) findViewById(R.id.large_tip_description);

        smallTipPriceTextView.setOnClickListener(this);
        largeTipPriceTextView.setOnClickListener(this);

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
                            String smallTipTitle = inventory.getSkuDetails(SMALL_TIP).getTitle();
                            String smallTipDescription = inventory.getSkuDetails(SMALL_TIP).getDescription();
                            String smallTipPrice = inventory.getSkuDetails(SMALL_TIP).getPrice();
                            String largeTipTitle = inventory.getSkuDetails(LARGE_TIP).getTitle();
                            String largeTipDescription = inventory.getSkuDetails(LARGE_TIP).getDescription();
                            String largeTipPrice = inventory.getSkuDetails(LARGE_TIP).getPrice();

                            smallTipTitleTextView.setText(smallTipTitle);
                            smallTipPriceTextView.setText(smallTipPrice);
                            smallTipDescriptionTextView.setText(smallTipDescription);
                            largeTipTitleTextView.setText(largeTipTitle);
                            largeTipPriceTextView.setText(largeTipPrice);
                            largeTipDescriptionTextView.setText(largeTipDescription);
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
        switch (v.getId()) {
            case R.id.small_tip_price:
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
            case R.id.large_tip_price:
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