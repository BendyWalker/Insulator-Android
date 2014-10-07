package com.bendywalker.insulator;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 07/10/2014.
 */
public class MainActivity extends Activity {
    EditText desiredBloodGlucoseLevelEntry, currentBloodGlucoseLevelEntry, carbohydratesInMealEntry;
    TextView suggestedInsulinDose;
    float currentBloodGlucoseLevel, carbohydratesInMeal;
    Calculator calculator = new Calculator();

    static boolean textChangeRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        desiredBloodGlucoseLevelEntry = (EditText) findViewById(R.id.entry_desired_blood_glucose_level);
        currentBloodGlucoseLevelEntry = (EditText) findViewById(R.id.entry_current_blood_glucose_level);
        carbohydratesInMealEntry = (EditText) findViewById(R.id.entry_carbohydrates_in_meal);
        suggestedInsulinDose = (TextView) findViewById(R.id.display_suggested_dose);

        desiredBloodGlucoseLevelEntry.addTextChangedListener(new MyTextWatcher());
        currentBloodGlucoseLevelEntry.addTextChangedListener(new MyTextWatcher());
        carbohydratesInMealEntry.addTextChangedListener(new MyTextWatcher());
    }

    private class MyTextWatcher implements TextWatcher {
        EditText editText;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText.length() > 0) {
                addDecimalPlace(s, editText);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_calculate).setEnabled(isCalculateEnabled());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calculate:
                calculator = new Calculator(currentBloodGlucoseLevel,
                        carbohydratesInMeal, this.getApplicationContext());
                suggestedInsulinDose.setText(String.valueOf
                        (calculator.getCalculatedInsulinDose()));
                return true;

            case R.id.action_reset:
                resetFields();
                return true;
        }

        return false;
    }

    private void resetFields() {
        currentBloodGlucoseLevelEntry.setText("");
        carbohydratesInMealEntry.setText("");
        suggestedInsulinDose.setText("0.0");
    }

    private boolean isCalculateEnabled() {
        return ((desiredBloodGlucoseLevelEntry.length() > 0) &&
                (currentBloodGlucoseLevelEntry.length() > 0) &&
                (carbohydratesInMealEntry.length() > 0));
    }


    /**
     * Adds a point to one decimal place of a string of characters.
     *
     * @param s
     * @param et
     */
    public static void addDecimalPlace(CharSequence s, EditText et) {
        List<Integer> storedPoints = new ArrayList<Integer>();

        if (!textChangeRunning) {
            textChangeRunning = true;

            StringBuilder decimalPlaceAuto = new StringBuilder(s.toString());

            while (decimalPlaceAuto.length() > 2 && decimalPlaceAuto.charAt(0) == '0' || decimalPlaceAuto.charAt(0) == '.') {
                decimalPlaceAuto.deleteCharAt(0);
            }

            for (int i = 0; i < decimalPlaceAuto.length(); i++) {
                if (decimalPlaceAuto.charAt(i) == '.') {
                    storedPoints.add(i);
                }
            }

            for (int i = 0; i < storedPoints.size(); i++) {
                decimalPlaceAuto.deleteCharAt(storedPoints.get(i));
            }

            while (decimalPlaceAuto.length() < 2) {
                decimalPlaceAuto.insert(0, '0');
            }

            decimalPlaceAuto.insert(decimalPlaceAuto.length() - 1, '.');

            et.setText(decimalPlaceAuto.toString());

            textChangeRunning = false;

            Selection.setSelection(et.getText(), decimalPlaceAuto.toString().length());

        }
    }
}
