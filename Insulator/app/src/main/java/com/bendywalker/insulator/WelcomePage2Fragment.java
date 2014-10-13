package com.bendywalker.insulator;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class WelcomePage2Fragment extends Fragment
{
    RelativeLayout halfUnitsCard, bloodGlucoseUnitsCard, continueCard;
    CheckBox halfUnitsCheckBox;
    RadioGroup bloodGlucoseUnitsRadioGroup;
    Button continueButton;

    SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_welcome_page_2, container, false);

        halfUnitsCard = (RelativeLayout) view.findViewById(R.id.card_half_units);
        halfUnitsCheckBox = (CheckBox) view.findViewById(R.id.card_half_units_checkbox);

        bloodGlucoseUnitsCard = (RelativeLayout) view
                .findViewById(R.id.card_blood_glucose_measurement);
        bloodGlucoseUnitsRadioGroup = (RadioGroup) view
                .findViewById(R.id.card_blood_glucose_measurement_radio_group);
        bloodGlucoseUnitsRadioGroup.check(R.id.card_blood_glucose_measurement_radio_button_mmol);

        continueCard = (RelativeLayout) view.findViewById(R.id.card_continue);
        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage3Fragment());
                savePreferences();
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        addAnimationsToCards();

        return view;
    }

    private void savePreferences()
    {
        preferences.edit().putBoolean(getString(R.string.preference_half_units),
                                      halfUnitsCheckBox.isChecked());

        String bloodGlucoseUnit = "";
        switch (bloodGlucoseUnitsRadioGroup.getCheckedRadioButtonId())
        {
            case R.id.card_blood_glucose_measurement_radio_button_mmol:
                bloodGlucoseUnit = "mmol";
                break;

            case R.id.card_blood_glucose_measurement_radio_button_mgdl:
                bloodGlucoseUnit = "mgdl";
                break;
        }
        preferences.edit()
                   .putString(getString(R.string.preference_blood_glucose_units), bloodGlucoseUnit);

        preferences.edit().apply();
    }

    private void addAnimationsToCards()
    {
        int offset = getResources().getInteger(R.integer.animation_offset);
        int offsetMultiplier = 1;

        List<Animation> animations = new ArrayList<Animation>();

        Animation halfUnitsAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);

        animations.add(halfUnitsAnimation);

        Animation bloodGlucoseUnitsAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        animations.add(bloodGlucoseUnitsAnimation);

        Animation continueAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        animations.add(continueAnimation);

        for (Animation animation : animations)
        {
            animation.setStartOffset(offset * offsetMultiplier);
            offsetMultiplier++;
        }

        halfUnitsCard.startAnimation(halfUnitsAnimation);
        bloodGlucoseUnitsCard.startAnimation(bloodGlucoseUnitsAnimation);
        continueCard.startAnimation(continueAnimation);
    }
}