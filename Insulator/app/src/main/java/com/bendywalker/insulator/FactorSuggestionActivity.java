package com.bendywalker.insulator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class FactorSuggestionActivity extends Activity implements Card.OnTextChangeListener, View.OnClickListener
{
    Card totalDailyDoseCard;
    TextView carbohydrateFactorSuggestion, correctiveFactorSuggestion;
    Button saveCarbohydrateFactorButton, saveCorrectiveFactorButton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    boolean isHalfUnitsEnabled, isMmolSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_factor_suggestion);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();

        isHalfUnitsEnabled = preferences
                .getBoolean(getString(R.string.preference_half_units), false);

        isMmolSelected = preferences
                .getString(getString(R.string.preference_blood_glucose_units), "mmol")
                .equals("mmol");

        totalDailyDoseCard = (Card) findViewById(R.id.card_total_daily_dose);
        totalDailyDoseCard.setOnTextChangeListener(this);

        carbohydrateFactorSuggestion = (TextView) findViewById(
                R.id.card_carbohydrate_factor_suggestion);
        saveCarbohydrateFactorButton = (Button) findViewById(R.id.button_save_carbohydrate_factor);
        saveCarbohydrateFactorButton.setOnClickListener(this);

        correctiveFactorSuggestion = (TextView) findViewById(
                R.id.card_corrective_factor_suggestion);
        saveCorrectiveFactorButton = (Button) findViewById(R.id.button_save_corrective_factor);
        saveCorrectiveFactorButton.setOnClickListener(this);
    }

    @Override
    public void onTextChange()
    {
        saveCarbohydrateFactorButton.setText(getString(R.string.button_save));
        saveCorrectiveFactorButton.setText(getString(R.string.button_save));
        saveCorrectiveFactorButton.setEnabled(totalDailyDoseCard.isEntryFilled());
        saveCarbohydrateFactorButton.setEnabled(totalDailyDoseCard.isEntryFilled());

        double carbohydrateFactor, correctiveFactor, totalDailyDose;
        totalDailyDose = totalDailyDoseCard.getFloatFromEntry();

        DecimalFormat decimalFormat = new DecimalFormat("##.0");

        carbohydrateFactor = (500 / totalDailyDose);

        if (isMmolSelected)
        {
            correctiveFactor = (100 / totalDailyDose);
        }
        else
        {
            correctiveFactor = ((100 / totalDailyDose) * 18);
        }

        String carbohydrateFactorString, correctiveFactorString;
        if (totalDailyDose == 0)
        {
            carbohydrateFactorString = "0.0";
            correctiveFactorString = "0.0";
        }
        else
        {
            carbohydrateFactorString = String.valueOf(decimalFormat.format(carbohydrateFactor));
            correctiveFactorString = String.valueOf(decimalFormat.format(correctiveFactor));
        }

        carbohydrateFactorSuggestion.setText(carbohydrateFactorString);
        correctiveFactorSuggestion.setText(correctiveFactorString);
    }

    @Override
    public void onClick(View v)
    {
        Button button = (Button) v;
        switch (v.getId())
        {
            case R.id.button_save_carbohydrate_factor:
                float carbohydrateFactor = Float.valueOf(
                        carbohydrateFactorSuggestion.getText().toString());
                editor.putFloat(getString(R.string.preference_carbohydrate_factor),
                                carbohydrateFactor);
                break;

            case R.id.button_save_corrective_factor:
                float correctiveFactor = Float
                        .valueOf(correctiveFactorSuggestion.getText().toString());
                editor.putFloat(getString(R.string.preference_corrective_factor), correctiveFactor);
                break;

            default:
                break;
        }

        editor.apply();

        button.setText(getString(R.string.button_saved));
        button.setEnabled(false);
    }
}
