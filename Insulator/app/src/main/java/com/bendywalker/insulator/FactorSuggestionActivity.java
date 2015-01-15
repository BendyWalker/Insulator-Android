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

import java.util.ArrayList;
import java.util.List;


public class FactorSuggestionActivity extends Activity implements Card.OnTextChangeListener, View.OnClickListener
{
    Card totalDailyDoseCard;
    RelativeLayout welcomeCard, carbohydrateFactorCard, correctiveFactorCard;
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

        isMmolSelected = preferences.getString(getString(R.string.preference_blood_glucose_units), "mmol").equals("mmol");

        welcomeCard = (RelativeLayout) findViewById(R.id.card_welcome);

        totalDailyDoseCard = (Card) findViewById(R.id.card_total_daily_dose);
        totalDailyDoseCard.setOnTextChangeListener(this);

        carbohydrateFactorCard = (RelativeLayout) findViewById(R.id.card_carbohydrate_factor);
        carbohydrateFactorSuggestion = (TextView) findViewById(
                R.id.card_carbohydrate_factor_suggestion);
        saveCarbohydrateFactorButton = (Button) findViewById(R.id.button_save_carbohydrate_factor);
        saveCarbohydrateFactorButton.setOnClickListener(this);

        correctiveFactorCard = (RelativeLayout) findViewById(R.id.card_corrective_factor);
        correctiveFactorSuggestion = (TextView) findViewById(
                R.id.card_corrective_factor_suggestion);
        saveCorrectiveFactorButton = (Button) findViewById(R.id.button_save_corrective_factor);
        saveCorrectiveFactorButton.setOnClickListener(this);

        addAnimationsToCards();
    }

    private void addAnimationsToCards()
    {
        int offset = getResources().getInteger(R.integer.animation_offset);
        int offsetMultiplier = 1;

        List<Animation> animations = new ArrayList<Animation>();

        Animation welcomeCardAnimation = AnimationUtils
                .loadAnimation(this, R.anim.slide_in_up);
        animations.add(welcomeCardAnimation);

        Animation totalDailyDoseCardAnimation = AnimationUtils
                .loadAnimation(this, R.anim.slide_in_up);
        animations.add(totalDailyDoseCardAnimation);

        Animation carbohydrateFactorAnimation = AnimationUtils
                .loadAnimation(this, R.anim.slide_in_up);
        animations.add(carbohydrateFactorAnimation);

        Animation correctiveFactorAnimation = AnimationUtils
                .loadAnimation(this, R.anim.slide_in_up);
        animations.add(correctiveFactorAnimation);

        for (Animation animation : animations)
        {
            animation.setStartOffset(offset * offsetMultiplier);
            offsetMultiplier++;
        }

        welcomeCard.startAnimation(welcomeCardAnimation);
        totalDailyDoseCard.startAnimation(totalDailyDoseCardAnimation);
        carbohydrateFactorCard.startAnimation(carbohydrateFactorAnimation);
        correctiveFactorCard.startAnimation(correctiveFactorAnimation);
    }

    @Override
    public void onTextChange()
    {
        saveCarbohydrateFactorButton.setText(getString(R.string.button_save));
        saveCorrectiveFactorButton.setText(getString(R.string.button_save));
        saveCorrectiveFactorButton.setEnabled(totalDailyDoseCard.isEntryFilled());
        saveCarbohydrateFactorButton.setEnabled(totalDailyDoseCard.isEntryFilled());

        Calculator calculator = new Calculator(getApplicationContext());

        double carbohydrateFactor, correctiveFactor, totalDailyDose;
        totalDailyDose = totalDailyDoseCard.getFloatFromEntry();
        carbohydrateFactor = calculator.roundNumber(500 / totalDailyDose);

        if (isMmolSelected)
        {
            correctiveFactor = calculator.roundNumber(100 / totalDailyDose);
        }
        else
        {
            correctiveFactor = calculator.roundNumber((100 / totalDailyDose) * 18);
        }

        String carbohydrateFactorString, correctiveFactorString;
        if (totalDailyDose == 0)
        {
            carbohydrateFactorString = "0.0";
            correctiveFactorString = "0.0";
        }
        else
        {
            carbohydrateFactorString = String.valueOf(carbohydrateFactor);
            correctiveFactorString = String.valueOf(correctiveFactor);
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
