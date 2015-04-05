package com.bendywalker.insulator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class WelcomePage2Fragment extends Fragment
{
    CardView bloodGlucoseUnitsCard, continueCard;
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

        bloodGlucoseUnitsCard = (CardView) view
                .findViewById(R.id.card_blood_glucose_measurement);
        bloodGlucoseUnitsRadioGroup = (RadioGroup) view
                .findViewById(R.id.card_blood_glucose_measurement_radio_group);
        bloodGlucoseUnitsRadioGroup.check(R.id.card_blood_glucose_measurement_radio_button_mmol);

        continueCard = (CardView) view.findViewById(R.id.card_continue);
        continueButton = (Button) view.findViewById(R.id.button_continue);
        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                savePreferences();
                ((WelcomeActivity) getActivity()).goForwardToFragment(new WelcomePage3Fragment());
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // addAnimationsToCards();

        return view;
    }

    private void savePreferences()
    {
        SharedPreferences.Editor editor = preferences.edit();

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
        editor.putString(getString(R.string.preference_blood_glucose_units), bloodGlucoseUnit);

        editor.apply();
    }

    private void addAnimationsToCards()
    {
        int offset = getResources().getInteger(R.integer.animation_offset);
        int offsetMultiplier = 1;

        List<Animation> animations = new ArrayList<Animation>();

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

        bloodGlucoseUnitsCard.startAnimation(bloodGlucoseUnitsAnimation);
        continueCard.startAnimation(continueAnimation);
    }
}