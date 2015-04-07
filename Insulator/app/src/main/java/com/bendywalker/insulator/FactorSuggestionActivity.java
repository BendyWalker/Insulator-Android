package com.bendywalker.insulator;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FactorSuggestionActivity extends ActionBarActivity implements Card.OnTextChangeListener, View.OnClickListener {
    Card totalDailyDoseCard;
    TextView carbohydrateFactorSuggestionTextView, correctiveFactorSuggestionTextView;
    Button saveCarbohydrateFactorButton, saveCorrectiveFactorButton;
    MyPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factor_suggestion);

        preferenceManager = new MyPreferenceManager(getApplicationContext());

        totalDailyDoseCard = (Card) findViewById(R.id.card_total_daily_dose);
        totalDailyDoseCard.setOnTextChangeListener(this);

        carbohydrateFactorSuggestionTextView = (TextView) findViewById(
                R.id.card_carbohydrate_factor_suggestion);
        saveCarbohydrateFactorButton = (Button) findViewById(R.id.button_save_carbohydrate_factor);
        saveCarbohydrateFactorButton.setOnClickListener(this);

        correctiveFactorSuggestionTextView = (TextView) findViewById(
                R.id.card_corrective_factor_suggestion);
        saveCorrectiveFactorButton = (Button) findViewById(R.id.button_save_corrective_factor);
        saveCorrectiveFactorButton.setOnClickListener(this);
    }

    @Override
    public void onTextChange() {
        double carbohydrateFactor, correctiveFactor, totalDailyDose;
        String carbohydrateFactorString, correctiveFactorString;

        saveCarbohydrateFactorButton.setText(getString(R.string.button_save));
        saveCorrectiveFactorButton.setText(getString(R.string.button_save));
        saveCorrectiveFactorButton.setEnabled(totalDailyDoseCard.isEntryFieldFilled());
        saveCarbohydrateFactorButton.setEnabled(totalDailyDoseCard.isEntryFieldFilled());

        totalDailyDose = totalDailyDoseCard.getValueFromEntryField();
        carbohydrateFactor = (500 / totalDailyDose);

        correctiveFactor = 0;
        switch (preferenceManager.getBloodGlucoseUnit()) {
            case mmol:
            correctiveFactor = (100 / totalDailyDose);
                break;
            case mgdl:
            correctiveFactor = ((100 / totalDailyDose) * 18);
                break;
        }

        if (totalDailyDose == 0) {
            carbohydrateFactorString = "0.0";
            correctiveFactorString = "0.0";
        } else {
            carbohydrateFactorString = String.valueOf(Calculator.getString(carbohydrateFactor));
            correctiveFactorString = String.valueOf(Calculator.getString(correctiveFactor));
        }

        carbohydrateFactorSuggestionTextView.setText(carbohydrateFactorString);
        correctiveFactorSuggestionTextView.setText(correctiveFactorString);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        switch (v.getId()) {
            case R.id.button_save_carbohydrate_factor:
                double carbohydrateFactor = Double.valueOf(
                        carbohydrateFactorSuggestionTextView.getText().toString());
                preferenceManager.setCarbohydrateFactor(carbohydrateFactor);
                break;

            case R.id.button_save_corrective_factor:
                double correctiveFactor = Double
                        .valueOf(correctiveFactorSuggestionTextView.getText().toString());
                preferenceManager.setCorrectiveFactor(correctiveFactor);
                break;

            default:
                break;
        }

        button.setText(getString(R.string.button_saved));
        button.setEnabled(false);
    }
}
