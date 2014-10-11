package com.bendywalker.insulator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;


public class WelcomePage2Fragment extends Fragment
{
    RelativeLayout halfUnitsCard, bloodGlucoseUnitsCard, continueCard;
    Button continueButton;
    RadioGroup bloodGlucoseUnitsRadioGroup;

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

            }
        });

        int slideTime = getResources().getInteger(R.integer.config_slide_time) / 3;

        Animation halfUnitsAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);

        Animation bloodGlucoseUnitsAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        bloodGlucoseUnitsAnimation.setStartOffset(slideTime);

        Animation continueAnimation = AnimationUtils
                .loadAnimation(getActivity(), R.anim.slide_in_up);
        continueAnimation.setStartOffset(slideTime * 2);

        halfUnitsCard.startAnimation(halfUnitsAnimation);
        bloodGlucoseUnitsCard.startAnimation(bloodGlucoseUnitsAnimation);
        continueCard.startAnimation(continueAnimation);

        return view;
    }
}