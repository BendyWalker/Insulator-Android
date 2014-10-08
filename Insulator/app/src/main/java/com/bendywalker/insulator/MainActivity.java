package com.bendywalker.insulator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {
    Card desiredBloodGlucoseLevelCard, currentBloodGlucoseLevelCard, carbohydratesInMealCard;
    TextView suggestedInsulinDose;
    float currentBloodGlucoseLevel, carbohydratesInMeal;
    Calculator calculator = new Calculator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        desiredBloodGlucoseLevelCard = (Card) findViewById(R.id.card_desired_blood_glucose_level);
        currentBloodGlucoseLevelCard = (Card) findViewById(R.id.card_current_blood_glucose_level);
        carbohydratesInMealCard = (Card) findViewById(R.id.card_carbohydrates_in_meal);
        suggestedInsulinDose = (TextView) findViewById(R.id.display_suggested_dose);
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
        currentBloodGlucoseLevelCard.resetEntry();
        carbohydratesInMealCard.resetEntry();
        suggestedInsulinDose.setText("0.0");
    }

    private boolean isCalculateEnabled() {
        return ((desiredBloodGlucoseLevelCard.isEntryFilled()) &&
                (currentBloodGlucoseLevelCard.isEntryFilled()) &&
                (carbohydratesInMealCard.isEntryFilled()));
    }
}
